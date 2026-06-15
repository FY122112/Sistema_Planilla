package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.Puesto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PuestoRepositoryPort {

    List<Puesto> findAll();

    Optional<Puesto> findById(Long id);

    Puesto save(Puesto puesto);

    void deleteById(Long id);

    Optional<Puesto> findByNombrePuesto(String name);

    List<Puesto> findBySalarioBaseGreaterThanEqual(BigDecimal salarioBase);

}
