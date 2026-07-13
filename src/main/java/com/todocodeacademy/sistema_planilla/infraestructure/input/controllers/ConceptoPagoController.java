package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.ConceptoPagoServicePort;
import com.todocodeacademy.sistema_planilla.domain.model.ConceptoPago;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.ConceptoPagoRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.ConceptoPagoResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.ConceptoPagoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/concepto")
@RequiredArgsConstructor
public class ConceptoPagoController {

    private final ConceptoPagoServicePort conServ;
    private final ConceptoPagoMapper mapper;

    // =========================
    // LISTAR TODOS
    // =========================

    @GetMapping
    public ResponseEntity<List<ConceptoPagoResponseDTO>> listarConcepto() {

        List<ConceptoPagoResponseDTO> lista =
                conServ.findAll()
                        .stream()
                        .map(mapper::toResponseDTO)
                        .toList();

        return ResponseEntity.ok(lista);
    }

    // =========================
    // BUSCAR POR ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<ConceptoPagoResponseDTO> buscarPorId(
            @PathVariable Long id
    ) {

        ConceptoPago concepto = conServ.findById(id);

        return ResponseEntity.ok(
                mapper.toResponseDTO(concepto)
        );
    }

    // =========================
    // BUSCAR POR NOMBRE
    // =========================

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ConceptoPagoResponseDTO> buscarPorNombre(
            @PathVariable String nombre
    ) {

        ConceptoPago concepto =
                conServ.findByNombreConcepto(nombre);

        return ResponseEntity.ok(
                mapper.toResponseDTO(concepto)
        );
    }

    // =========================
    // BUSCAR POR TIPO
    // =========================

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ConceptoPagoResponseDTO>> buscarPorTipo(
            @PathVariable TipoConcepto tipo
    ) {

        List<ConceptoPagoResponseDTO> lista =
                conServ.findByTipo(tipo)
                        .stream()
                        .map(mapper::toResponseDTO)
                        .toList();

        return ResponseEntity.ok(lista);
    }

    // =========================
    // BUSCAR POR NOMBRE Y TIPO
    // =========================

    @GetMapping("/buscar")
    public ResponseEntity<ConceptoPagoResponseDTO> buscarPorNombreYTipo(

            @RequestParam String nombre,

            @RequestParam TipoConcepto tipo
    ) {

        ConceptoPago concepto =
                conServ.findByNombreConceptoAndTipo(
                        nombre,
                        tipo
                );

        return ResponseEntity.ok(
                mapper.toResponseDTO(concepto)
        );
    }

    // =========================
    // BUSCAR POR ESSALUD
    // =========================

    @GetMapping("/essalud/{afecto}")
    public ResponseEntity<List<ConceptoPagoResponseDTO>>
    buscarPorEssalud(

            @PathVariable Boolean afecto
    ) {

        List<ConceptoPagoResponseDTO> lista =
                conServ.findByAfectoEssalud(afecto)
                        .stream()
                        .map(mapper::toResponseDTO)
                        .toList();

        return ResponseEntity.ok(lista);
    }

    // =========================
    // CREAR
    // =========================

    @PostMapping
    public ResponseEntity<ConceptoPagoResponseDTO>
    createConcepto(

            @RequestBody ConceptoPagoRequestDTO request
    ) {

        ConceptoPago concepto =
                mapper.toDomain(request);

        concepto = conServ.save(concepto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponseDTO(concepto));
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<ConceptoPagoResponseDTO>
    updateConcepto(

            @PathVariable Long id,

            @RequestBody ConceptoPagoRequestDTO request
    ) {

        ConceptoPago concepto =
                mapper.toDomain(request);

        ConceptoPago actualizado =
                conServ.update(id, concepto);

        return ResponseEntity.ok(
                mapper.toResponseDTO(actualizado)
        );
    }

    // =========================
    // DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConcepto(
            @PathVariable Long id
    ) {

        conServ.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}