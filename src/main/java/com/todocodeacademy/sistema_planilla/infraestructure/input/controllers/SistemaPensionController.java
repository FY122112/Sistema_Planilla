package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.service.SistemaPensionService;
import com.todocodeacademy.sistema_planilla.domain.model.SistemaPension;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.SistemaPensionRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.SistemaPensionResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.SistemaPensionMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/sistema-pension")
@RequiredArgsConstructor
public class SistemaPensionController {

    private final SistemaPensionService service;
    private final SistemaPensionMapper mapper;

    // =========================
    // 📄 FIND ALL
    // =========================
    @GetMapping
    public ResponseEntity<List<SistemaPensionResponseDTO>> findAll() {

        List<SistemaPensionResponseDTO> response =
                service.findAll()
                        .stream()
                        .map(mapper::toResponseDTO)   // ← ya no static
                        .toList();

        return ResponseEntity.ok(response);
    }

    // =========================
    // 🔍 FIND BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<SistemaPensionResponseDTO> findById(
            @PathVariable Long id
    ) {

        SistemaPension entity = service.findById(id);

        return ResponseEntity.ok(
                mapper.toResponseDTO(entity)   // ← ya no static
        );
    }

    // =========================
    // 💾 SAVE
    // =========================
    @PostMapping
    public ResponseEntity<SistemaPensionResponseDTO> save(
            @Valid
            @RequestBody
            SistemaPensionRequestDTO request
    ) {

        SistemaPension entity =
                mapper.toEntity(request);   // ← ya no static

        entity = service.save(entity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        mapper.toResponseDTO(entity)
                );
    }

    // =========================
    // ✏️ UPDATE
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<SistemaPensionResponseDTO> update(
            @PathVariable Long id,

            @Valid
            @RequestBody
            SistemaPensionRequestDTO request
    ) {

        SistemaPension entity =
                mapper.toEntity(request);   // ← ya no static

        entity = service.update(id, entity);

        return ResponseEntity.ok(
                mapper.toResponseDTO(entity)
        );
    }

    // =========================
    // ❌ DELETE
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        service.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // =========================
    // 🧠 CALCULAR DESCUENTO
    // =========================
    @PostMapping("/{id}/calcular-descuento")
    public ResponseEntity<BigDecimal> calcularDescuento(
            @PathVariable Long id,
            @RequestParam BigDecimal sueldo
    ) {

        BigDecimal descuento =
                service.calcularDescuento(id, sueldo);

        return ResponseEntity.ok(descuento);
    }
}