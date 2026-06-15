package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;


import com.todocodeacademy.sistema_planilla.aplication.ports.input.EmpleadoServicePort;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.EmpleadoMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.CreateEmpleadoRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.UpdateEmpleadoRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.EmpleadoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoServicePort empleadoService;

    private final EmpleadoMapper mapper;

    // ==========================================
    // LISTAR TODOS
    // ==========================================

    @GetMapping
    public List<EmpleadoResponse> findAll() {

        return empleadoService.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ==========================================
    // BUSCAR POR ID
    // ==========================================

    @GetMapping("/{id}")
    public EmpleadoResponse findById(
            @PathVariable Long id
    ) {

        return mapper.toResponse(
                empleadoService.findById(id)
        );
    }

    // ==========================================
    // CREAR
    // ==========================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpleadoResponse save(
            @RequestBody CreateEmpleadoRequest request
    ) {

        return mapper.toResponse(
                empleadoService.save(
                        mapper.toDomain(request)
                )
        );
    }

    // ==========================================
    // ACTUALIZAR
    // ==========================================

    @PutMapping("/{id}")
    public EmpleadoResponse update(
            @PathVariable Long id,
            @RequestBody UpdateEmpleadoRequest request
    ) {

        return mapper.toResponse(
                empleadoService.update(
                        id,
                        mapper.toDomain(request)
                )
        );
    }

    // ==========================================
    // ELIMINAR
    // ==========================================

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {

        empleadoService.deleteById(id);
    }

    // ==========================================
    // EMPLEADOS ACTIVOS / INACTIVOS
    // ==========================================

    @GetMapping("/estado/{estado}")
    public List<EmpleadoResponse> findByEstado(
            @PathVariable Boolean estado
    ) {

        return empleadoService.findByEstado(estado)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ==========================================
    // HIJOS CALIFICADOS
    // ==========================================

    @GetMapping("/hijos/{tieneHijos}")
    public List<EmpleadoResponse> findByTieneHijosCalificados(
            @PathVariable Boolean tieneHijos
    ) {

        return empleadoService
                .findByTieneHijosCalificados(tieneHijos)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ==========================================
    // BUSQUEDA GENERAL
    // ==========================================

    @GetMapping("/search")
    public List<EmpleadoResponse> search(
            @RequestParam String query
    ) {

        return empleadoService
                .searchByDniOrNameOrLastName(query)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}