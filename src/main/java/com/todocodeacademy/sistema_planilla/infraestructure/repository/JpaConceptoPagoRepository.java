package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.ConceptoPagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaConceptoPagoRepository
        extends JpaRepository<ConceptoPagoEntity, Long> {

    Optional<ConceptoPagoEntity> findByNombreConcepto(
            String nombreConcepto
    );

    Optional<ConceptoPagoEntity> findByNombreConceptoAndTipoConcepto(
            String nombreConcepto,
            TipoConcepto tipoConcepto
    );

    List<ConceptoPagoEntity> findByTipoConcepto(
            TipoConcepto tipoConcepto
    );

    List<ConceptoPagoEntity> findByAfectoEssalud(
            Boolean afectoEssalud
    );
}