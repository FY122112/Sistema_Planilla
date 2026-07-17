package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.command.MarcarAsistneciaCommand;
import com.todocodeacademy.sistema_planilla.aplication.ports.input.AsistenciaServicePort;
import com.todocodeacademy.sistema_planilla.domain.model.Asistencia;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.AsistenciaJustificarRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.AsistenciaRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.AsistenciaResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.AsistenciaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Marcado de asistencia: dato administrativo/de RR.HH., exclusivo del Administrador por
// ahora (no forma parte del portal de autoservicio del empleado).
@PreAuthorize("hasRole('ADMINISTRADOR')")
@RestController
@RequestMapping("/api/asistencia")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaServicePort asiServ;
    private final AsistenciaMapper mapper;

    @PostMapping
    public ResponseEntity<AsistenciaResponseDTO> marcarAistencia(@RequestBody AsistenciaRequestDTO asistenciaRequestDTO) {

        MarcarAsistneciaCommand marcarAsistneciaCommand = new MarcarAsistneciaCommand( asistenciaRequestDTO.getNumeroDocumento(),asistenciaRequestDTO.getTipoMarca());

        Asistencia asistencia = asiServ.marcarAsistencia(marcarAsistneciaCommand);

        AsistenciaResponseDTO requestDTO = mapper.toResponse(asistencia);

        return ResponseEntity.ok(requestDTO);

    }

    // @RequestParam en vez de @RequestBody: un GET con cuerpo lo descartan silenciosamente
    // los navegadores (XHR/fetch no lo envían), así que el frontend nunca podría llamarlo.
    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDTO>> mostrarReporteDiario(@RequestParam LocalDate fecha){

        List<Asistencia> asistencias = asiServ.mostrarReporteDiario(fecha);

        List<AsistenciaResponseDTO> asisRes = asistencias.stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(asisRes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDTO> editarAsistencia(@PathVariable Long id,@RequestBody AsistenciaJustificarRequest asisJus) {

        Asistencia asistencia = asiServ.justificarAsistencia(id, asisJus.getMotivo());

        return  ResponseEntity.ok(mapper.toResponse(asistencia));

    }

    // Empleados sin ninguna marca de asistencia en el mes/año indicado, de entre los ids
    // dados — usado por el frontend para el banner de "asistencia incompleta" (HU-008).
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
}
