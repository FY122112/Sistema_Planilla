package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.ControlVacacionalServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.ControlVacacionalRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.ControlVacacion;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ControlVacacionalService
        implements ControlVacacionalServicePort {

    private final ControlVacacionalRepositoryPort repository;

    // =========================
    // CRUD
    // =========================

    @Override
    public List<ControlVacacion> findAll() {

        return repository.findAll();
    }

    @Override
    public ControlVacacion findById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Control vacacional no encontrado"
                        )
                );
    }

    @Override
    public ControlVacacion save(
            ControlVacacion controlVacacion
    ) {

        return repository.save(controlVacacion);
    }

    @Override
    public ControlVacacion update(
            Long id,
            ControlVacacion controlVacacion
    ) {

        ControlVacacion actual =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Control vacacional no encontrado"
                                )
                        );

        // =========================
        // PERIODO
        // =========================

        if (
                controlVacacion.getFechaInicioPeriodo() != null
                        &&
                        controlVacacion.getFechaFinPeriodo() != null
        ) {

            actual.actualizarPeriodo(
                    controlVacacion.getFechaInicioPeriodo(),
                    controlVacacion.getFechaFinPeriodo()
            );
        }

        // =========================
        // DÍAS GANADOS
        // =========================

        if (controlVacacion.getDiasGanados() != null) {

            actual.asignarDias(
                    controlVacacion.getDiasGanados()
                            - actual.getDiasGanados()
            );
        }

        return repository.save(actual);
    }

    @Override
    public void deleteById(Long id) {

        repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Control vacacional no encontrado"
                        )
                );

        repository.deleteById(id);
    }

    // =========================
    // CONSULTAS NEGOCIO
    // =========================

    @Override
    public List<ControlVacacion> findByEmpleado(
            Empleado empleado
    ) {

        return repository.findByEmpleado(empleado);
    }

    @Override
    public List<ControlVacacion> findVacacionesPendientes(
            Empleado empleado,
            Integer dias
    ) {

        return repository.findVacacionesPendientes(
                empleado,
                dias
        );
    }

    @Override
    public ControlVacacion findPeriodoActivo(
            Empleado empleado,
            LocalDate fecha
    ) {

        return repository.findPeriodoActivo(
                        empleado,
                        fecha
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "No existe periodo vacacional activo"
                        )
                );
    }

    // =========================
    // OPERACIONES NEGOCIO
    // =========================

    @Override
    public ControlVacacion gozarDias(
            Long idControlVacacion,
            Integer dias
    ) {

        ControlVacacion control =
                repository.findById(idControlVacacion)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Control vacacional no encontrado"
                                )
                        );

        control.gozarDias(dias);

        return repository.save(control);
    }

    @Override
    public ControlVacacion revertirDias(
            Long idControlVacacion,
            Integer dias
    ) {

        ControlVacacion control =
                repository.findById(idControlVacacion)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Control vacacional no encontrado"
                                )
                        );

        control.revertirDias(dias);

        return repository.save(control);
    }
}