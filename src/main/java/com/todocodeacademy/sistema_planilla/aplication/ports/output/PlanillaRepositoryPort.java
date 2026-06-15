package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoPlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Planilla;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.PlanillaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlanillaRepositoryPort {

    List<Planilla> findAll();

    Optional<Planilla> findById(Long id);

    Planilla save(Planilla banco);

    void deleteById(Long id);

    Optional<Planilla> findByMesAndAnioAndTipoPlanilla(Integer mes, Integer anio, TipoPlanilla tipoPlanilla);

    List<Planilla> findByFechaGeneradaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<Planilla> findByAnioAndTipoPlanilla(Integer anio, TipoPlanilla tipoPlanilla);

    Optional<Planilla> findByMesAndAnio(Integer mes, Integer anio);


}
