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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDTO>> mostrarReporteDiario(@RequestBody LocalDate fecha){

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
}
