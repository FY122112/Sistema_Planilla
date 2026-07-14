package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.BoletaServicePort;
import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.CreateBoletaRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.UpdateBoletaRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.BoletaResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.BoletaMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boletas")
@RequiredArgsConstructor
public class BoletaController {

    private final BoletaServicePort service;
    private final BoletaMapper mapper;

    @GetMapping
    public ResponseEntity<List<BoletaResponseDTO>> findAll() {

        return ResponseEntity.ok(
                service.findAll()
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoletaResponseDTO> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                mapper.toResponse(service.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<BoletaResponseDTO> save(
            @Valid @RequestBody CreateBoletaRequest request
    ) {

        Boleta guardada = service.save(mapper.toDomain(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(guardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoletaResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBoletaRequest request
    ) {

        Boleta actualizada = service.update(id, mapper.toDomain(request));

        return ResponseEntity.ok(
                mapper.toResponse(actualizada)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {

        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
