package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.SolicitudAjusteServicePort;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.CreateSolicitudAjusteRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.SolicitudAjusteResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.SolicitudAjusteMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.security.AuthenticatedPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitudes-ajuste")
@RequiredArgsConstructor
public class SolicitudAjusteController {

    private final SolicitudAjusteServicePort service;
    private final SolicitudAjusteMapper mapper;

    @PreAuthorize("hasRole('EMPLEADO')")
    @PostMapping
    public ResponseEntity<SolicitudAjusteResponseDTO> crear(
            @Valid @RequestBody CreateSolicitudAjusteRequest request,
            Authentication authentication
    ) {

        Long idEmpleado = idEmpleadoDelPrincipal(authentication);

        var creada = service.crear(idEmpleado, request.idBoleta(), request.mensaje());

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(creada));
    }

    @PreAuthorize("hasRole('EMPLEADO')")
    @GetMapping("/me")
    public ResponseEntity<List<SolicitudAjusteResponseDTO>> misSolicitudes(Authentication authentication) {

        Long idEmpleado = idEmpleadoDelPrincipal(authentication);

        return ResponseEntity.ok(
                service.findByEmpleadoId(idEmpleado)
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<SolicitudAjusteResponseDTO>> findAll() {

        return ResponseEntity.ok(
                service.findAll()
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/pendientes/count")
    public ResponseEntity<Map<String, Long>> contarPendientes() {
        return ResponseEntity.ok(Map.of("pendientes", service.contarPendientes()));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping("/{id}/atender")
    public ResponseEntity<SolicitudAjusteResponseDTO> atender(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.marcarComoAtendida(id)));
    }

    private Long idEmpleadoDelPrincipal(Authentication authentication) {

        if (authentication.getPrincipal() instanceof AuthenticatedPrincipal principal) {
            return principal.idEmpleado();
        }

        return null;
    }
}
