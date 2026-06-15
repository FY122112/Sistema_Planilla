package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.Asistencia;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpleadoEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AsistenciaRepositoryPort {

    List<Asistencia> findAll();

    Optional<Asistencia> findById(Long id);

    Optional<Asistencia> findByEmpleadoAndFecha(Empleado empleado, LocalDate fecha);

    Asistencia save(Asistencia banco);

    void deleteById(Long id);

}
