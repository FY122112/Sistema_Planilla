package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.HistorialPuesto;
import com.todocodeacademy.sistema_planilla.domain.model.MovimientoPlanilla;

import java.util.List;
import java.util.Optional;

public interface MovimientoPlanillaRepositoryPort {

    List<MovimientoPlanilla> findAll();

    Optional<MovimientoPlanilla> findById(Long id);

    MovimientoPlanilla save(MovimientoPlanilla banco);

    void deleteById(Long id);

}
