package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.SistemaPension;

import java.math.BigDecimal;
import java.util.List;

public interface SistemaPensionServicePort {

    List<SistemaPension> findAll();

    SistemaPension findById(Long id);

    SistemaPension save(SistemaPension sistemaPension);

    SistemaPension update(Long id,SistemaPension sistemaPension);

    void deleteById(Long id);

    BigDecimal calcularDescuento(Long id, BigDecimal sueldo);


}
