package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.command.MarcarAsistneciaCommand;
import com.todocodeacademy.sistema_planilla.aplication.ports.input.AsistenciaServicePort;
import com.todocodeacademy.sistema_planilla.domain.model.Asistencia;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.AsistenciaJustificarRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.AsistenciaRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.MarcarMiAsistenciaRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.AsistenciaResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.AsistenciaMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.security.AuthenticatedPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Marcado/reporte/justificación de asistencia: gestión administrativa, exclusiva del
// Administrador. La marca de la PROPIA asistencia (más abajo) es la excepción: cualquier
// cuenta vinculada a un Empleado puede marcarla, igual que ya ocurre con boletas.
@RestController
@RequestMapping("/api/asistencia")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaServicePort asiServ;
    private final AsistenciaMapper mapper;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<AsistenciaResponseDTO> marcarAistencia(@RequestBody AsistenciaRequestDTO asistenciaRequestDTO) {

        MarcarAsistneciaCommand marcarAsistneciaCommand = new MarcarAsistneciaCommand( asistenciaRequestDTO.getNumeroDocumento(),asistenciaRequestDTO.getTipoMarca());

        Asistencia asistencia = asiServ.marcarAsistencia(marcarAsistneciaCommand);

        AsistenciaResponseDTO requestDTO = mapper.toResponse(asistencia);

        return ResponseEntity.ok(requestDTO);

    }

    // @RequestParam en vez de @RequestBody: un GET con cuerpo lo descartan silenciosamente
    // los navegadores (XHR/fetch no lo envían), así que el frontend nunca podría llamarlo.
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDTO>> mostrarReporteDiario(@RequestParam LocalDate fecha){

        List<Asistencia> asistencias = asiServ.mostrarReporteDiario(fecha);

        List<AsistenciaResponseDTO> asisRes = asistencias.stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(asisRes);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDTO> editarAsistencia(@PathVariable Long id,@RequestBody AsistenciaJustificarRequest asisJus) {

        Asistencia asistencia = asiServ.justificarAsistencia(id, asisJus.getMotivo());

        return  ResponseEntity.ok(mapper.toResponse(asistencia));

    }

    // Empleados sin ninguna marca de asistencia en el mes/año indicado, de entre los ids
    // dados — usado por el frontend para el banner de "asistencia incompleta" (HU-008).
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/faltantes")
    public ResponseEntity<List<Long>> idsEmpleadosSinAsistencia(
            @RequestParam List<Long> idsEmpleados,
            @RequestParam Integer mes,
            @RequestParam Integer anio
    ) {

        return ResponseEntity.ok(
                asiServ.idsEmpleadosSinAsistencia(idsEmpleados, mes, anio)
        );
    }

    // =========================
    // PORTAL DE AUTOSERVICIO DEL EMPLEADO
    // =========================
    //
    // Igual que boletas: no depende del nombre del rol, solo de que la cuenta tenga un
    // Empleado vinculado (idEmpleado en el JWT). El numeroDocumento no viene del request —
    // se resuelve del propio Empleado vinculado, para que nadie pueda marcar la asistencia
    // de otra persona.

    @PostMapping("/mi-marca")
    public ResponseEntity<AsistenciaResponseDTO> marcarMiAsistencia(
            @RequestBody MarcarMiAsistenciaRequest request,
            Authentication authentication
    ) {

        Long idEmpleado = idEmpleadoDelPrincipal(authentication);

        if (idEmpleado == null) {
            throw new AccessDeniedException("Esta cuenta no está vinculada a un empleado");
        }

        Asistencia asistencia = asiServ.marcarAsistenciaPropia(idEmpleado, request.getTipoMarca());

        return ResponseEntity.ok(mapper.toResponse(asistencia));
    }

    private Long idEmpleadoDelPrincipal(Authentication authentication) {

        if (authentication.getPrincipal() instanceof AuthenticatedPrincipal principal) {
            return principal.idEmpleado();
        }

        return null;
    }
}
