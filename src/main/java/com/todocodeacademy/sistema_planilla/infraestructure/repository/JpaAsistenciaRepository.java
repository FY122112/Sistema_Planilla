package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.AsistenciaEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaAsistenciaRepository extends JpaRepository<AsistenciaEntity,Long> {

    Optional<AsistenciaEntity> findByEmpleadoAndFecha(EmpleadoEntity empleado, LocalDate fecha);

    List<AsistenciaEntity> findByEmpleado_IdEmpleadoInAndFechaBetween(
            List<Long> idsEmpleados, LocalDate fechaInicio, LocalDate fechaFin
    );

}
