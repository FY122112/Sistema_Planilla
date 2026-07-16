package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.BoletaServicePort;
import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.CreateBoletaRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.UpdateBoletaRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.BoletaResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.EnlaceCompartidoResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.BoletaMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.security.AuthenticatedPrincipal;
import com.todocodeacademy.sistema_planilla.infraestructure.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boletas")
@RequiredArgsConstructor
public class BoletaController {

    private final BoletaServicePort service;
    private final BoletaMapper mapper;
    private final JwtUtils jwtUtils;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<BoletaResponseDTO>> findAll() {

        return ResponseEntity.ok(
                service.findAll()
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoletaResponseDTO> findById(
            @PathVariable Long id,
            Authentication authentication
    ) {

        BoletaResponseDTO response = mapper.toResponse(service.findById(id));
        verificarAccesoBoleta(authentication, response);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<BoletaResponseDTO> save(
            @Valid @RequestBody CreateBoletaRequest request
    ) {

        Boleta guardada = service.save(mapper.toDomain(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(guardada));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<BoletaResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBoletaRequest request
    ) {

        Boleta actualizada = service.update(id, mapper.toDomain(request));

        return ResponseEntity.ok(
                mapper.toResponse(actualizada)
        );
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {

        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarPdf(
            @PathVariable Long id,
            Authentication authentication
    ) {

        var boletaResponse = mapper.toResponse(service.findById(id));
        verificarAccesoBoleta(authentication, boletaResponse);

        byte[] pdf = service.generarPdf(id);

        String nombreArchivo = "boleta-%s-%s-%s.pdf".formatted(
                boletaResponse.getNumeroDocumento() != null ? boletaResponse.getNumeroDocumento() : id,
                boletaResponse.getPeriodoMes(),
                boletaResponse.getPeriodoAnio()
        );

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(nombreArchivo).build().toString())
                .body(pdf);
    }

    // =========================
    // ENLACE PÚBLICO PARA WHATSAPP (HU-060)
    // =========================
    //
    // /pdf normal exige JWT de sesión; quien recibe el enlace por WhatsApp no tiene una.
    // Este endpoint sirve el PDF de UNA boleta específica validando un token de alcance
    // mínimo (ver JwtUtils#createBoletaShareToken) en vez de la sesión del usuario.

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/{id}/compartir")
    public ResponseEntity<EnlaceCompartidoResponseDTO> crearEnlaceCompartido(
            @PathVariable Long id
    ) {

        service.findById(id); // 404 si la boleta no existe

        String token = jwtUtils.createBoletaShareToken(id);

        return ResponseEntity.ok(
                EnlaceCompartidoResponseDTO.builder()
                        .token(token)
                        .rutaDescarga("/api/boletas/compartida/pdf?token=" + token)
                        .build()
        );
    }

    @GetMapping("/compartida/pdf")
    public ResponseEntity<byte[]> descargarPdfCompartido(
            @RequestParam String token
    ) {

        Long idBoleta = jwtUtils.validateBoletaShareToken(token);

        var boletaResponse = mapper.toResponse(service.findById(idBoleta));
        byte[] pdf = service.generarPdf(idBoleta);

        String nombreArchivo = "boleta-%s-%s-%s.pdf".formatted(
                boletaResponse.getNumeroDocumento() != null ? boletaResponse.getNumeroDocumento() : idBoleta,
                boletaResponse.getPeriodoMes(),
                boletaResponse.getPeriodoAnio()
        );

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename(nombreArchivo).build().toString())
                .body(pdf);
    }

    // =========================
    // DESCARGA MASIVA EN ZIP (HU-010)
    // =========================

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/zip")
    public ResponseEntity<byte[]> descargarZip(
            @RequestParam Integer mes,
            @RequestParam Integer anio
    ) {

        byte[] zip = service.exportarZip(mes, anio);

        String nombreArchivo = "boletas-%s-%s.zip".formatted(mes, anio);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/zip"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(nombreArchivo).build().toString())
                .body(zip);
    }

    // =========================
    // PORTAL DE AUTOSERVICIO DEL EMPLEADO
    // =========================

    @PreAuthorize("hasRole('EMPLEADO')")
    @GetMapping("/me")
    public ResponseEntity<List<BoletaResponseDTO>> misBoletas(Authentication authentication) {

        Long idEmpleado = idEmpleadoDelPrincipal(authentication);

        return ResponseEntity.ok(
                service.findByEmpleadoId(idEmpleado)
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    @PreAuthorize("hasRole('EMPLEADO')")
    @PatchMapping("/{id}/firmar")
    public ResponseEntity<BoletaResponseDTO> firmar(
            @PathVariable Long id,
            Authentication authentication
    ) {

        Long idEmpleado = idEmpleadoDelPrincipal(authentication);

        return ResponseEntity.ok(
                mapper.toResponse(service.firmarComoEmpleado(id, idEmpleado))
        );
    }

    // =========================
    // AUTORIZACIÓN POR DUEÑO
    // =========================

    private void verificarAccesoBoleta(Authentication authentication, BoletaResponseDTO boleta) {

        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (esAdmin) {
            return;
        }

        Long idEmpleado = idEmpleadoDelPrincipal(authentication);

        if (idEmpleado == null || !idEmpleado.equals(boleta.getIdEmpleado())) {
            throw new AccessDeniedException("No puedes acceder a la boleta de otro empleado");
        }
    }

    private Long idEmpleadoDelPrincipal(Authentication authentication) {

        if (authentication.getPrincipal() instanceof AuthenticatedPrincipal principal) {
            return principal.idEmpleado();
        }

        return null;
    }
}
