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
    public ResponseEntity<List<PermissionResponseDTO>> listarPermisos() {
        List<PermissionResponseDTO> permisos = perSer.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(permisos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> buscarPermisoPorId(@PathVariable Long id) {
        Permission permiso = perSer.findById(id);
        return ResponseEntity.ok(mapper.toResponse(permiso));
    }

    @PostMapping
    public ResponseEntity<PermissionResponseDTO> crearPermiso(@Valid @RequestBody PermissionRequestDTO dto) {
        Permission permiso = mapper.toDomain(dto);
        Permission saved = perSer.save(permiso); // ✅ Persistimos en BD
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> actualizarPermiso(@PathVariable Long id,
                                                                   @Valid @RequestBody PermissionRequestDTO dto) {
        Permission permiso = mapper.toDomain(dto);
        Permission update = perSer.update(id, permiso);
        return ResponseEntity.ok(mapper.toResponse(update));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPermiso(@PathVariable Long id) {
        perSer.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nombre/{nombre}") // ✅ Evitamos conflicto con @GetMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> buscarPermisoPorNombre(@PathVariable String nombre) {
        Permission permiso = perSer.findByNombre(nombre);
        return ResponseEntity.ok(mapper.toResponse(permiso));
    }
}
