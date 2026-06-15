package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BoletaRepositoryPort {

    List<Boleta> findAll();

    Optional<Boleta> findById(Long id);

    Boleta save(Boleta banco);

    void deleteById(Long id);

    Optional<Boleta> getBoletaByDetallePlanilla(DetallePlanilla detallePlanilla);

    List<Boleta> getBoletasByFechaEmisionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<Boleta> getBoletasByFirmadaStatus(Boolean firmada);

    List<Boleta> getBoletasByEnviadaStatus(Boolean enviada);


}
