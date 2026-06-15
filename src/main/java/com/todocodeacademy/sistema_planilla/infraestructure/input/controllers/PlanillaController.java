package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.PlanillaServicePort;
import com.todocodeacademy.sistema_planilla.domain.model.Planilla;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.PlanillaRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.PlanillaResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.PlanillaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planillas")
@RequiredArgsConstructor
public class PlanillaController {

    private final PlanillaServicePort service;
    private final PlanillaMapper mapper;

    // =========================
    // LISTAR TODAS
    // =========================

    @GetMapping
    public ResponseEntity<List<PlanillaResponseDTO>> findAll() {

        List<PlanillaResponseDTO> response =
                service.findAll()
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    // =========================
    // OBTENER POR ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<PlanillaResponseDTO> findById(
            @PathVariable Long id
    ) {

        PlanillaResponseDTO response =
                mapper.toResponse(
                        service.findById(id)
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // GENERAR PLANILLA
    // =========================

    @PostMapping("/generar")
    public ResponseEntity<PlanillaResponseDTO> generarPlanilla(
            @RequestBody PlanillaRequestDTO request
    ) {

        Planilla planilla =
                service.generarPlanilla(
                        request.getMes(),
                        request.getAnio(),
                        request.getTipoPlanilla()
                );

        PlanillaResponseDTO response =
                mapper.toResponse(planilla);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================
    // CERRAR PLANILLA
    // =========================

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<PlanillaResponseDTO> cerrarPlanilla(
            @PathVariable Long id
    ) {

        PlanillaResponseDTO response =
                mapper.toResponse(
                        service.cerrarPlanilla(id)
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // ABRIR PLANILLA
    // =========================

    @PatchMapping("/{id}/abrir")
    public ResponseEntity<PlanillaResponseDTO> abrirPlanilla(
            @PathVariable Long id
    ) {

        PlanillaResponseDTO response =
                mapper.toResponse(
                        service.abrirPlanilla(id)
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // ELIMINAR PLANILLA
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {

        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}