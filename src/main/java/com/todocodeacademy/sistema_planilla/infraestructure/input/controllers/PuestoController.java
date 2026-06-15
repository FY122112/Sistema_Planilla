package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.PuestoServicePort;
import com.todocodeacademy.sistema_planilla.domain.model.Puesto;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.PuestoRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.PuestoResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.PuestoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/puestos")
@RequiredArgsConstructor
public class PuestoController {

    private final PuestoServicePort puestoService;

    private final PuestoMapper mapper;

    // =========================
    // 📋 LISTAR TODOS
    // =========================

    @GetMapping
    public ResponseEntity<List<PuestoResponseDTO>> findAll() {

        return ResponseEntity.ok(
                puestoService.findAll()
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    // =========================
    // 🔍 BUSCAR POR ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<PuestoResponseDTO> findById(
            @PathVariable Long id
    ) {

        Puesto puesto = puestoService.findById(id);

        return ResponseEntity.ok(
                mapper.toResponse(puesto)
        );
    }

    // =========================
    // ➕ CREAR
    // =========================

    @PostMapping
    public ResponseEntity<PuestoResponseDTO> crear(
            @Valid @RequestBody PuestoRequestDTO dto
    ) {

        Puesto puesto = mapper.toDomain(dto);

        Puesto saved = puestoService.save(puesto);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));

    }

    // =========================
    // ✏️ ACTUALIZAR
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<PuestoResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PuestoRequestDTO dto
    ) {

        Puesto puesto = mapper.toDomain(dto);

        Puesto updated = puestoService.update( id,puesto);

        return ResponseEntity.ok(
                mapper.toResponse(updated)
        );
    }

    // =========================
    // ❌ ELIMINAR
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {

        puestoService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    // =========================
// 🔍 BUSCAR POR NOMBRE
// =========================

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<PuestoResponseDTO> findByNombre(
            @PathVariable String nombre
    ) {

        Puesto puesto = puestoService.findByNombrePuesto(nombre);

        return ResponseEntity.ok(
                mapper.toResponse(puesto)
        );
    }

    // =========================
// 💰 BUSCAR POR SALARIO BASE
// =========================

    @GetMapping("/{salarioBase}")
    public ResponseEntity<?> findBySalarioBase(
            @PathVariable BigDecimal salarioBase
    ) {

        return ResponseEntity.ok(
                puestoService.findBySalarioBaseGreaterThanEqual(salarioBase)
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }


}