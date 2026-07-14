package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.PermissionServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.input.RoleServicePort;
import com.todocodeacademy.sistema_planilla.domain.model.Permission;
import com.todocodeacademy.sistema_planilla.domain.model.Role;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.RoleRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.RoleResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.RoleMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

// Gestionar roles (verlos, crearlos, editarlos, eliminarlos) es una capacidad
// exclusiva de administradores: antes cualquiera con un token válido podía hacerlo.
@PreAuthorize("hasRole('ADMINISTRADOR')")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleServicePort roleService;

    private final PermissionServicePort permissionService;

    private final RoleMapper mapper;

    @GetMapping
    public ResponseEntity<?> findAll() {

        return ResponseEntity.ok(
                roleService.findAll()
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> findById(
            @PathVariable Long id
    ) {

        Role role = roleService.findById(id);

        return ResponseEntity.ok(
                mapper.toResponse(role)
        );
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> save(
            @Valid @RequestBody RoleRequestDTO dto
    ) {

        Set<Permission> permissions = dto.getPermissionIds()
                .stream()
                .map(permissionService::findById)
                .collect(Collectors.toSet());

        Role role = mapper.toDomain(dto, permissions);

        Role saved = roleService.save(role);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));

    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequestDTO dto
    ) {

        Set<Permission> permissions = dto.getPermissionIds()
                .stream()
                .map(permissionService::findById)
                .collect(Collectors.toSet());

        Role role = mapper.toDomain(dto, permissions);

        Role updated = roleService.update(role, id);

        return ResponseEntity.ok(
                mapper.toResponse(updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {

        roleService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
