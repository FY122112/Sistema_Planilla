package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.ConceptoPago;

import java.util.List;

public interface ConceptoPagoServicePort {

    List<ConceptoPago> findAll();

    ConceptoPago findById(Long id);

    ConceptoPago save(ConceptoPago conceptoPago);

    ConceptoPago update(Long id, ConceptoPago conceptoPago);

    void deleteById(Long id);

}
