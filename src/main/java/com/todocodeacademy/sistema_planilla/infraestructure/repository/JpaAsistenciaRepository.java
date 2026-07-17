package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.AsistenciaEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaAsistenciaRepository extends JpaRepository<AsistenciaEntity,Long> {

    Optional<AsistenciaEntity> findByEmpleadoAndFecha(EmpleadoEntity empleado, LocalDate fecha);

    List<AsistenciaEntity> findByEmpleado_IdEmpleadoInAndFechaBetween(
            List<Long> idsEmpleados, LocalDate fechaInicio, LocalDate fechaFin
    );

    // Proyección liviana (solo la fecha): usada para calcular ausentismo automático en
    // planilla sin traer ni mapear el Empleado/Puesto/SistemaPension asociados.
    @Query("select a.fecha from AsistenciaEntity a " +
            "where a.empleado.idEmpleado = :idEmpleado and a.fecha between :fechaInicio and :fechaFin " +
            "and a.horaEntrada is not null")
    List<LocalDate> findFechaByEmpleado_IdEmpleadoAndFechaBetweenAndHoraEntradaIsNotNull(
            @Param("idEmpleado") Long idEmpleado,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    @Query("select coalesce(sum(a.minutosTardanzas), 0) from AsistenciaEntity a " +
            "where a.empleado.idEmpleado = :idEmpleado and a.fecha between :fechaInicio and :fechaFin")
    Integer sumMinutosTardanzaByEmpleadoAndFechaBetween(
            @Param("idEmpleado") Long idEmpleado,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

}
