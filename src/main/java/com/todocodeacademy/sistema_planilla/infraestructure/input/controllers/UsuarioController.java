package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.UsuarioSecServicePort;
import com.todocodeacademy.sistema_planilla.domain.model.UsuarioSec;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.CreateUsuarioRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.UpdateUsuarioRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.UsuarioResponse;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Gestionar usuarios (verlos, crearlos, editarlos, eliminarlos) es una capacidad
// exclusiva de administradores: antes cualquiera con un token válido podía hacerlo.
@PreAuthorize("hasRole('ADMINISTRADOR')")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioSecServicePort service;

    private final UsuarioMapper mapper;

    @GetMapping
    public List<UsuarioResponse> findAll() {

        return service.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public UsuarioResponse findById(@PathVariable Long id) {

        return mapper.toResponse(
                service.findById(id)
        );
    }

    @GetMapping("/username/{username}")
    public UsuarioResponse findByUsername(
            @PathVariable String username
    ) {

        return mapper.toResponse(
                service.findByUsername(username)
        );
    }

    @GetMapping("/email/{email}")
    public UsuarioResponse findByEmail(
            @PathVariable String email
    ) {

        return mapper.toResponse(
                service.findByEmail(email)
        );
    }

    @PostMapping
    public UsuarioResponse save(
            @RequestBody CreateUsuarioRequest request
    ) {

        UsuarioSec usuario = mapper.toDomain(request);

        return mapper.toResponse(
                service.save(usuario)
        );
    }

    @PutMapping("/{id}")
    public UsuarioResponse update(
            @PathVariable Long id,
            @RequestBody UpdateUsuarioRequest request
    ) {

        UsuarioSec usuario = mapper.toDomain(request);

        return mapper.toResponse(
                service.update(id, usuario)
        );
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id
    ) {

        service.deleteById(id);
    }
}