package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.PermissionServicePort;
import com.todocodeacademy.sistema_planilla.domain.model.Permission;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.PermissionRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.PermissionResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.PermissionMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/permiso")
public class PermissionController {

    private final PermissionServicePort perSer;

    private final PermissionMapper mapper;

    @GetMapping
    public ResponseEntity<List<PermissionResponseDTO>> ListarPermisos() {

        List<PermissionResponseDTO> permisos = perSer.findAll()
                                    .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(permisos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> BuscarPermisos(@PathVariable Long id) {

        Permission permiso = perSer.findById(id);

        PermissionResponseDTO permisoDTO = mapper.toResponse(permiso);

        return ResponseEntity.ok(permisoDTO);

    }

    @PostMapping
    public ResponseEntity<PermissionResponseDTO> crearPermisos(@RequestBody PermissionRequestDTO dto) {

        Permission permiso = mapper.toDomain(dto);

        PermissionResponseDTO permisoDTO = mapper.toResponse(permiso);

        return ResponseEntity.status(HttpStatus.CREATED).body(permisoDTO);

    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> actualizarPermisos(@PathVariable Long id ,@Valid @RequestBody PermissionRequestDTO dto) {

        Permission permiso = mapper.toDomain(dto);

        Permission update = perSer.update(id,permiso);

        PermissionResponseDTO permisoDTO = mapper.toResponse(update);

        return ResponseEntity.ok(permisoDTO);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPermisos(@PathVariable Long id) {
        perSer.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<PermissionResponseDTO> buscarPermisos(@PathVariable String nombre) {

        Permission permiso = perSer.findByNombre(nombre);

        PermissionResponseDTO permisoDTO = mapper.toResponse(permiso);

        return ResponseEntity.ok(permisoDTO);
    }
}
