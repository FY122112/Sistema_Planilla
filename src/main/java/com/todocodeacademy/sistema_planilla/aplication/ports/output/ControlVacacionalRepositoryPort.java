package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.ControlVacacion;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.domain.model.ParametroLegal;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.ControlVacacionEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpleadoEntity;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ControlVacacionalRepositoryPort {

    List<ControlVacacion> findAll();

    Optional<ControlVacacion> findById(Long id);

    ControlVacacion save(ControlVacacion banco);

    void deleteById(Long id);

    List<ControlVacacion> findVacacionesPendientes(Empleado empleado, Integer dias);

    List<ControlVacacion> findByEmpleado(Empleado empleado);

    Optional<ControlVacacion> findByEmpleadoAndFechaInicioPeriodoAndFechaFinPeriodo(Empleado empleado, LocalDate inicio, LocalDate fin);

    Optional<ControlVacacion> findPeriodoActivo(
            Empleado empleado,
            LocalDate fecha
    );

}
