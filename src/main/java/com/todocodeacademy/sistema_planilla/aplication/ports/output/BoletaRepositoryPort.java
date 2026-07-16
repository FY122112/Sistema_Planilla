package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoBoleta;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BoletaRepositoryPort {

    List<Boleta> findAll();

    Optional<Boleta> findById(Long id);

    Boleta save(Boleta boleta);

    void deleteById(Long id);

    Optional<Boleta> getBoletaByDetallePlanilla(DetallePlanilla detallePlanilla);

    List<Boleta> getBoletasByFechaEmisionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<Boleta> getBoletasByEstadoBoleta(EstadoBoleta estadoBoleta);

    List<Boleta> findByEmpleadoId(Long idEmpleado);

    List<Boleta> findByPeriodo(Integer periodoMes, Integer periodoAnio);

}
