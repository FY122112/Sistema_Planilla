package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoPlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Planilla;

import java.time.LocalDate;
import java.util.List;

public interface PlanillaServicePort {

    // =========================
    // CRUD
    // =========================

    List<Planilla> findAll();

    Planilla findById(Long id);

    Planilla save(Planilla planilla);

    Planilla update(Long id, Planilla planilla);

    void deleteById(Long id);

    // =========================
    // BÚSQUEDAS
    // =========================

    Planilla findByMesAnioAndTipo(
            Integer mes,
            Integer anio,
            TipoPlanilla tipoPlanilla
    );

    List<Planilla> findByFechaGeneradaBetween(
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    List<Planilla> findByAnioAndTipo(
            Integer anio,
            TipoPlanilla tipoPlanilla
    );

    // =========================
    // LÓGICA DE NEGOCIO
    // =========================

    Planilla generarPlanilla(
            Integer mes,
            Integer anio,
            TipoPlanilla tipoPlanilla
    );

    Planilla cerrarPlanilla(Long idPlanilla);

    Planilla abrirPlanilla(Long idPlanilla);

    Planilla obtenerPlanillaCompleta(Long idPlanilla);
}