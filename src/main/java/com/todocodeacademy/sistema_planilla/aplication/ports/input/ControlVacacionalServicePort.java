package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.ControlVacacion;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;

import java.time.LocalDate;
import java.util.List;

public interface ControlVacacionalServicePort {

    // =========================
    // CRUD
    // =========================

    List<ControlVacacion> findAll();

    ControlVacacion findById(Long id);

    ControlVacacion save(ControlVacacion controlVacacion);

    ControlVacacion update(
            Long id,
            ControlVacacion controlVacacion
    );

    void deleteById(Long id);

    // =========================
    // CONSULTAS NEGOCIO
    // =========================

    List<ControlVacacion> findByEmpleado(
            Empleado empleado
    );

    List<ControlVacacion> findVacacionesPendientes(
            Empleado empleado,
            Integer dias
    );

    ControlVacacion findPeriodoActivo(
            Empleado empleado,
            LocalDate fecha
    );

    // =========================
    // OPERACIONES NEGOCIO
    // =========================

    ControlVacacion gozarDias(
            Long idControlVacacion,
            Integer dias
    );

    ControlVacacion revertirDias(
            Long idControlVacacion,
            Integer dias
    );
}