package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.ParamatroLegalServicePort;
import com.todocodeacademy.sistema_planilla.domain.model.ParametroLegal;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.ParametroLegalRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.ParametroLegalResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.ParametroLegalMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/parametros-legales")
@RequiredArgsConstructor
public class ParametroLegalController {


    private final ParamatroLegalServicePort parametroService;

    private final ParametroLegalMapper mapper;

    // =========================
    // 📋 LISTAR TODOS
    // =========================

    @GetMapping
    public ResponseEntity<List<ParametroLegalResponseDTO>> findAll() {

        List<ParametroLegalResponseDTO> response = parametroService.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ParametroLegalResponseDTO> findById(
            @PathVariable Long id
    ) {

        ParametroLegal parametro =
                parametroService.findById(id);

        return ResponseEntity.ok(
                mapper.toResponse(parametro)
        );
    }

    @PostMapping
    public ResponseEntity<ParametroLegalResponseDTO> save(
            @Valid @RequestBody ParametroLegalRequestDTO dto
    ) {

        ParametroLegal parametro =
                mapper.toDomain(dto);

        ParametroLegal saved =
                parametroService.save(parametro);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParametroLegalResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ParametroLegalRequestDTO dto
    ) {

        ParametroLegal parametro =
                mapper.toDomain(dto);

        ParametroLegal updated =
                parametroService.update(id, parametro);

        return ResponseEntity.ok(
                mapper.toResponse(updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {

        parametroService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<List<ParametroLegalResponseDTO>> findByCodigo(
            @PathVariable String codigo
    ) {

        return ResponseEntity.ok(
                parametroService.findByCodigo(codigo)
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/vigente")
    public ResponseEntity<ParametroLegalResponseDTO>
    findVigenteByCodigoAndFecha(

            @RequestParam String codigo,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha
    ) {

        ParametroLegal parametro =
                parametroService
                        .findVigenteByCodigoAndFecha(
                                codigo,
                                fecha
                        );

        return ResponseEntity.ok(
                mapper.toResponse(parametro)
        );
    }

    @GetMapping("/ultimo/{codigo}")
    public ResponseEntity<ParametroLegalResponseDTO>
    findTopByCodigo(

            @PathVariable String codigo
    ) {

        ParametroLegal parametro =
                parametroService
                        .findTopByCodigoOrderByFechaInicioVigenciaDesc(
                                codigo
                        );

        return ResponseEntity.ok(
                mapper.toResponse(parametro)
        );
    }

    // =========================
    // 🔍 BUSCAR POR DESCRIPCIÓN
    // =========================

    @GetMapping("/descripcion")
    public ResponseEntity<List<ParametroLegalResponseDTO>> findByDescripcion(
            @RequestParam String descripcion
    ) {

        return ResponseEntity.ok(
                parametroService
                        .findByDescripcionContainingIgnoreCase(
                                descripcion
                        )
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    // =========================
    // 🔍 BUSCAR POR RANGO FECHAS
    // =========================

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<ParametroLegalResponseDTO>> findByFechaInicioVigenciaBetween(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {

        return ResponseEntity.ok(
                parametroService
                        .findByFechaInicioVigenciaBetween(
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

}
