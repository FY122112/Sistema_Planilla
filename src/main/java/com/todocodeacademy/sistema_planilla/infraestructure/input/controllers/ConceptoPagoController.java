package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.ConceptoPagoServicePort;
import com.todocodeacademy.sistema_planilla.aplication.service.ConceptoPagoService;
import com.todocodeacademy.sistema_planilla.domain.model.ConceptoPago;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.ConceptoPagoRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.ConceptoPagoResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.ConceptoPagoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/concepto/")
@RequiredArgsConstructor
public class ConceptoPagoController {

    private final ConceptoPagoServicePort conServ;

    private final ConceptoPagoMapper mapper;


    @GetMapping
    public ResponseEntity<List<ConceptoPagoResponseDTO>> listarConcepto() {
        List<ConceptoPagoResponseDTO> listar = conServ.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(listar);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConceptoPagoResponseDTO> BuscarConcepto(@PathVariable Long id) {

        ConceptoPago conceptoPago = conServ.findById(id);

        ConceptoPagoResponseDTO responseDTO = mapper.toResponseDTO(conceptoPago);

        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping
    public ResponseEntity<ConceptoPagoResponseDTO> createConcepto(@RequestBody ConceptoPagoRequestDTO request) {

        ConceptoPago concepto = mapper.toDomain(request);

        concepto = conServ.save(concepto);

        ConceptoPagoResponseDTO responseDTO = mapper.toResponseDTO(concepto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ConceptoPagoResponseDTO>  updateConcepto(@PathVariable Long id, @RequestBody ConceptoPagoRequestDTO request) {

        ConceptoPago conceptoPago = mapper.toDomain(request);

        ConceptoPago con = conServ.update(id,conceptoPago);

        ConceptoPagoResponseDTO responseDTO = mapper.toResponseDTO(con);

        return ResponseEntity.ok(responseDTO);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConcepto(@PathVariable Long id) {

        conServ.deleteById(id);

        return ResponseEntity.noContent().build();

    }


}
