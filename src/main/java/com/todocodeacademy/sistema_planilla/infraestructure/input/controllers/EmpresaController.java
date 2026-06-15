package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;


import com.todocodeacademy.sistema_planilla.domain.model.Empresa;


import com.todocodeacademy.sistema_planilla.aplication.service.EmpresaService;

import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.EmpresaRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.EmpresaResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.EmpresaMapper;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresa")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService service;

    private final EmpresaMapper mapper;
    // =========================
    // 📄 LISTAR TODAS
    // =========================
    @GetMapping
    public ResponseEntity<List<EmpresaResponseDTO>>
    findAll() {

        List<EmpresaResponseDTO> response =
                service.findAll()
                        .stream()
                        .map(mapper::toResponseDTO)
                        .toList();

        return ResponseEntity.ok(response);
    }

    // =========================
    // 🔍 BUSCAR POR ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO>
    findById(
            @PathVariable Long id
    ) {

        Empresa empresa = service.findById(id);

        return ResponseEntity.ok(
                mapper.toResponseDTO(empresa)
        );
    }

    // =========================
    // 💾 CREAR
    // =========================
    @PostMapping
    public ResponseEntity<EmpresaResponseDTO>
    save(

            @Valid
            @RequestBody
            EmpresaRequestDTO request
    ) {

        Empresa empresa =
                mapper.toEntity(request);

        empresa = service.save(empresa);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        mapper.toResponseDTO(empresa)
                );
    }

    // =========================
    // ✏️ ACTUALIZAR
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO>
    update(

            @PathVariable Long id,

            @Valid
            @RequestBody
            EmpresaRequestDTO request
    ) {

        Empresa empresaExistente =
                service.findById(id);

        empresaExistente.actualizarDatos(
                request.getDireccion(),
                request.getTelefono(),
                request.getCorreo()
        );

        Empresa empresaActualizada =
                service.update(id, empresaExistente);

        return ResponseEntity.ok(
                mapper.toResponseDTO(
                        empresaActualizada
                )
        );
    }

    // =========================
    // ❌ ELIMINAR
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    delete(
            @PathVariable Long id
    ) {

        service.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // =========================
    // 🔍 BUSCAR POR RUC
    // =========================
    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<EmpresaResponseDTO>
    findByRuc(
            @PathVariable String ruc
    ) {

        Empresa empresa = service
                .buscarPorRuc(ruc);

        return ResponseEntity.ok(
                mapper.toResponseDTO(empresa)
        );
    }
}