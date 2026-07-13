package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.ConceptoPago;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;

import java.util.List;

public interface ConceptoPagoServicePort {

    List<ConceptoPago> findAll();

    ConceptoPago findById(Long id);

    ConceptoPago save(ConceptoPago conceptoPago);

    ConceptoPago update(Long id, ConceptoPago conceptoPago);

    void deleteById(Long id);

    // NUEVOS

    ConceptoPago findByNombreConcepto(String nombreConcepto);

    ConceptoPago findByNombreConceptoAndTipo(
            String nombreConcepto,
            TipoConcepto tipo
    );

    List<ConceptoPago> findByTipo(TipoConcepto tipo);

    List<ConceptoPago> findByAfectoEssalud(Boolean afectoEssalud);
}