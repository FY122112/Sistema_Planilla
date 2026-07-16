package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.BancoServicePort;
import com.todocodeacademy.sistema_planilla.domain.model.Banco;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.BancoRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.BancoResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.BancoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Catálogo de bancos: configuración global, exclusiva del Administrador.
@PreAuthorize("hasRole('ADMINISTRADOR')")
@RestController
@RequestMapping("/api/banco")
@RequiredArgsConstructor
public class BancoController {

    private final BancoServicePort bankSer;

    private final BancoMapper mapper;

    @GetMapping
    public ResponseEntity<List<BancoResponseDTO>> listarBanco(){

        List<BancoResponseDTO> lista = bankSer.findAll().stream()
                .map(mapper::bancoResponseDTO)
                .toList();

        return ResponseEntity.ok(lista);

    }

    @GetMapping("/{id}")
    public ResponseEntity<BancoResponseDTO> buscarBanco(@PathVariable Long id){

        Banco bank = bankSer.findById(id);

        return ResponseEntity.ok(mapper.bancoResponseDTO(bank));

    }

    @PostMapping
    public ResponseEntity<BancoResponseDTO> guardarBanco(
            @RequestBody BancoRequestDTO requestDTO
    ){

        Banco bank = mapper.bancoDTO(requestDTO);

        Banco save = bankSer.save(bank);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        mapper.bancoResponseDTO(save)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BancoResponseDTO> actualizarBanco(
            @PathVariable Long id,
            @RequestBody BancoRequestDTO requestDTO
    ){

        Banco bank = mapper.bancoDTO(requestDTO);

        Banco actualizado = bankSer.update(id, bank);

        return ResponseEntity.ok(
                mapper.bancoResponseDTO(actualizado)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBanco(@PathVariable Long id){

        bankSer.deleteById(id);

        return ResponseEntity.noContent().build();

    }

}
