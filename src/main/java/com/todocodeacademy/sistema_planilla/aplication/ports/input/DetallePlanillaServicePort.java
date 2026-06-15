package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;

import java.util.List;

public interface DetallePlanillaServicePort {

    List<DetallePlanilla> findAll();

    DetallePlanilla findById(Long id);

    DetallePlanilla save(DetallePlanilla detallePlanilla);

    DetallePlanilla update(Long id, DetallePlanilla detallePlanilla);

    void deleteById(Long id);

}
