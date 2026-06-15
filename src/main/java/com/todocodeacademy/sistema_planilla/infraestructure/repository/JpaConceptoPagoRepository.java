package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.ConceptoPagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaConceptoPagoRepository extends JpaRepository<ConceptoPagoEntity, Long> {

    /**
     * Busca un concepto de pago por su nombre.
     */
    Optional<ConceptoPagoEntity> findByNombreConcepto(String nombreConcepto);

    /**
     * Busca un concepto de pago por su nombre y tipo.
     */
    Optional<ConceptoPagoEntity> findByNombreConceptoAndTipoConcepto(String nombreConcepto, String tipoConcepto);

    /**
     * Busca todos los conceptos de pago de un tipo específico.
     */
    List<ConceptoPagoEntity> findByTipoConcepto(String tipoConcepto);

    /**
     * Busca conceptos de pago que sean afectos a EsSalud.
     */
    List<ConceptoPagoEntity> findByAfectoEssalud(Boolean afectoEssalud);
}
