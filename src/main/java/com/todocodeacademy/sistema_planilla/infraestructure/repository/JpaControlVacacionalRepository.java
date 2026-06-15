package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.ControlVacacionEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaControlVacacionalRepository extends JpaRepository<ControlVacacionEntity,Long> {

    @Query("""
       SELECT c
       FROM ControlVacacionEntity c
       WHERE c.empleado = :empleado
       AND (c.diasGanados - c.diasGozados) > :dias
       """)
    List<ControlVacacionEntity> findVacacionesPendientes(
            @Param("empleado") EmpleadoEntity empleado,
            @Param("dias") Integer dias
    );

    /**
     * Busca todos los registros de control de vacaciones para un empleado.
     * @param empleado El empleado.
     * @return Una lista de registros de ControlVacacion para el empleado.
     */
    List<ControlVacacionEntity> findByEmpleado(EmpleadoEntity empleado);

    Optional<ControlVacacionEntity>
    findByEmpleadoAndFechaInicioPeriodoAndFechaFinPeriodo(
            EmpleadoEntity empleado,
            LocalDate inicio,
            LocalDate fin
    );

    @Query("""
       SELECT c
       FROM ControlVacacionEntity c
       WHERE c.empleado = :empleado
       AND :fecha BETWEEN c.fechaInicioPeriodo
                       AND c.fechaFinPeriodo
       """)
    Optional<ControlVacacionEntity>
    findPeriodoActivo(
            @Param("empleado") EmpleadoEntity empleado,
            @Param("fecha") LocalDate fecha
    );

}
