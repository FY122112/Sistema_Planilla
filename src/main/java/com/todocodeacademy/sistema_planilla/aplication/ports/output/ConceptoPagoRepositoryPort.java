package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.ConceptoPago;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;

import java.util.List;
import java.util.Optional;

public interface ConceptoPagoRepositoryPort {

    List<ConceptoPago> findAll();

    Optional<ConceptoPago> findById(Long id);

    ConceptoPago save(ConceptoPago banco);

    void deleteById(Long id);

    Optional<ConceptoPago> findByNombreConcepto(String nombreConcepto);

    Optional<ConceptoPago> findByNombreConceptoAndTipo(String nombreConcepto, TipoConcepto tipo);

    List<ConceptoPago> findByTipo(TipoConcepto tipo);

    List<ConceptoPago> findByAfectoEssalud(Boolean afectoEssalud);
}
