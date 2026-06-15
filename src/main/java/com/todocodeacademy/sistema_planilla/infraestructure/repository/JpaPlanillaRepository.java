package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoPlanilla;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.PlanillaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaPlanillaRepository extends JpaRepository<PlanillaEntity,Long> {

    /**
     * Busca una planilla por mes, año y tipo.
     * @param mes El mes de la planilla.
     * @param anio El año de la planilla.
     * @param tipoPlanilla El tipo de planilla (ej. MENSUAL, CTS, GRATIFICACION).
     * @return Un Optional que contiene la Planilla encontrada, o vacío si no existe.
     */
    Optional<PlanillaEntity> findByMesAndAnioAndTipoPlanilla(Integer mes, Integer anio, TipoPlanilla tipoPlanilla);

    /**
     * Busca todas las planillas generadas en un rango de fechas.
     * @param fechaInicio La fecha de inicio del rango (inclusive).
     * @param fechaFin La fecha de fin del rango (inclusive).
     * @return Una lista de planillas generadas en el rango especificado.
     */
    List<PlanillaEntity> findByFechaGeneradaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Busca todas las planillas de un tipo específico para un año dado.
     * @param anio El año de la planilla.
     * @param tipoPlanilla El tipo de planilla.
     * @return Una lista de planillas que coinciden con el año y tipo.
     */
    List<PlanillaEntity> findByAnioAndTipoPlanilla(Integer anio, TipoPlanilla tipoPlanilla);

    /**
     * Busca una planilla por mes y año.
     * Útil para verificar si existe alguna planilla para un período dado,
     * sin importar el tipo de planilla.
     * @param mes El mes de la planilla.
     * @param anio El año de la planilla.
     * @return Un Optional que contiene la primera Planilla encontrada para ese mes y año, o vacío si no existe.
     */
    Optional<PlanillaEntity> findByMesAndAnio(Integer mes, Integer anio);


}
