package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.Puesto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PuestoServicePort {

    List<Puesto> findAll();

    Puesto findById(Long id);

    Puesto save(Puesto puesto);

    Puesto update(Long id,Puesto puesto);

    void deleteById(Long id);

    Puesto findByNombrePuesto(String name);

    List<Puesto> findBySalarioBaseGreaterThanEqual(BigDecimal salarioBase);

}
