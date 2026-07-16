package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;


import com.todocodeacademy.sistema_planilla.aplication.command.EmpleadoImportRow;
import com.todocodeacademy.sistema_planilla.aplication.command.ImportacionEmpleadosResumen;
import com.todocodeacademy.sistema_planilla.aplication.ports.input.EmpleadoServicePort;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.EmpleadoCsvParser;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.EmpleadoMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.CreateEmpleadoRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.UpdateEmpleadoRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.EmpleadoResponse;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.ImportacionEmpleadoResultadoDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.ImportacionEmpleadosResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// Empleado es "administración de personal": exclusivo del Administrador, igual que
// Usuarios/Roles — un EMPLEADO con sesión de autoservicio no debe poder listar ni editar
// a otros trabajadores.
@PreAuthorize("hasRole('ADMINISTRADOR')")
@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoServicePort empleadoService;
    private final EmpleadoMapper mapper;
    private final EmpleadoCsvParser csvParser;

    @GetMapping
    public ResponseEntity<List<EmpleadoResponse>> findAll() {

        return ResponseEntity.ok(
                empleadoService.findAll()
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponse> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                mapper.toResponse(
                        empleadoService.findById(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponse> save(
            @Valid @RequestBody CreateEmpleadoRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        mapper.toResponse(
                                empleadoService.save(
                                        mapper.toDomain(request)
                                )
                        )
                );
    }

    @PutMapping("/{id}")
    public EmpleadoResponse update(
            @PathVariable Long id,
            @RequestBody UpdateEmpleadoRequest request
    ) {

        Empleado actual = empleadoService.findById(id);

        Empleado actualizado =
                mapper.updateDomain(request, actual);

        return mapper.toResponse(
                empleadoService.update(id, actualizado)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        empleadoService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<EmpleadoResponse>> findByEstado(
            @PathVariable Boolean estado
    ) {

        return ResponseEntity.ok(
                empleadoService.findByEstado(estado)
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/hijos/{tieneHijos}")
    public ResponseEntity<List<EmpleadoResponse>> findByTieneHijosCalificados(
            @PathVariable Boolean tieneHijos
    ) {

        return ResponseEntity.ok(
                empleadoService
                        .findByTieneHijosCalificados(tieneHijos)
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmpleadoResponse>> search(
            @RequestParam String query
    ) {

        return ResponseEntity.ok(
                empleadoService
                        .searchByDniOrNameOrLastName(query)
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    // =========================
    // IMPORTACIÓN MASIVA (HU-028)
    // =========================

    @PostMapping(value = "/importar-csv", consumes = "multipart/form-data")
    public ResponseEntity<ImportacionEmpleadosResponseDTO> importarCsv(
            @RequestParam("archivo") MultipartFile archivo
    ) {

        List<EmpleadoImportRow> filas = csvParser.parse(archivo);
        ImportacionEmpleadosResumen resumen = empleadoService.importarCsv(filas);

        return ResponseEntity.ok(
                ImportacionEmpleadosResponseDTO.builder()
                        .totalFilas(resumen.totalFilas())
                        .exitosos(resumen.exitosos())
                        .fallidos(resumen.fallidos())
                        .detalle(resumen.detalle().stream()
                                .map(r -> ImportacionEmpleadoResultadoDTO.builder()
                                        .fila(r.fila())
                                        .numeroDocumento(r.numeroDocumento())
                                        .exitoso(r.exitoso())
                                        .mensaje(r.mensaje())
                                        .build())
                                .toList())
                        .build()
        );
    }
}