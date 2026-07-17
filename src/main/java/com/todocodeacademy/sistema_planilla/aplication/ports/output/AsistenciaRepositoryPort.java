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

    List<Long> findIdsEmpleadosConAsistencia(List<Long> idsEmpleados, LocalDate fechaInicio, LocalDate fechaFin);

    // Usados por PlanillaService para derivar automáticamente el ausentismo y la
    // tardanza del mes a partir de las marcas reales de Asistencia (HU-031/HU-032/HU-033).
    List<LocalDate> findFechasConEntrada(Long idEmpleado, LocalDate fechaInicio, LocalDate fechaFin);

    int sumMinutosTardanza(Long idEmpleado, LocalDate fechaInicio, LocalDate fechaFin);

}
