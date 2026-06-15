package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.domain.model.Asistencia;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoAsistencia;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.AsistenciaEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaAsistenciaRepository extends JpaRepository<AsistenciaEntity,Long> {


    /**
     * Busca la asistencia de un empleado para una fecha específica.
     * @param empleado El empleado.
     * @param fecha La fecha de la asistencia.
     * @return Un Optional que contiene la Asistencia encontrada, o vacío si no existe.
     */
    Optional<AsistenciaEntity> findByEmpleadoAndFecha(EmpleadoEntity empleado, LocalDate fecha);

    //Optional<AsistenciaEntity> findByfindByEmpleadoAndFecha(EmpleadoEntity empleado,LocalDate hoy);


    /**
     * Busca todas las asistencias de un empleado en un rango de fechas.
     * @param empleado El empleado.
     * @param fechaInicio La fecha de inicio del rango (inclusive).
     * @param fechaFin La fecha de fin del rango (inclusive).
     * @return Una lista de asistencias para el empleado en el rango especificado.
     */
   // List<AsistenciaEntity> findByEmpleadoAndFechaBetween(Empleado empleado, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Busca asistencias con estado de tardanza o ausencia para un empleado en un rango de fechas.
     * Esto es útil para calcular descuentos en la planilla.
     * @param empleado El empleado.
     * @param fechaInicio La fecha de inicio del rango (inclusive).
     * @param fechaFin La fecha de fin del rango (inclusive).
     * @param estados Los estados de asistencia a buscar (ej. Asistencia.EstadoAsistencia.TARDANZA, Asistencia.EstadoAsistencia.AUSENTE).
     * @return Una lista de asistencias que coinciden con los estados y el rango de fechas.
     */
    //List<Asistencia> findByEmpleadoAndFechaBetweenAndEstadoIn(Empleado empleado, LocalDate fechaInicio, LocalDate fechaFin, List<EstadoAsistencia> estados);

}
