package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoBoleta;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.BoletaEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.DetallePlanillaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaBoletaRepository extends JpaRepository<BoletaEntity,Long> {

    Optional<BoletaEntity> getBoletaByDetallePlanilla(DetallePlanillaEntity detallePlanilla);

    List<BoletaEntity> getBoletasByFechaEmisionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<BoletaEntity> getBoletasByEstadoBoleta(EstadoBoleta estadoBoleta);

    List<BoletaEntity> findByDetallePlanilla_Empleado_IdEmpleado(Long idEmpleado);

    List<BoletaEntity> findByPeriodoMesAndPeriodoAnio(Integer periodoMes, Integer periodoAnio);

}

