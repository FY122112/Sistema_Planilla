package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;

import java.util.List;
import java.util.Optional;

public interface DetallePlanillaRepositoryPort {

    List<DetallePlanilla> findAll();

    Optional<DetallePlanilla> findById(Long id);

    DetallePlanilla save(DetallePlanilla banco);

    void deleteById(Long id);

}
