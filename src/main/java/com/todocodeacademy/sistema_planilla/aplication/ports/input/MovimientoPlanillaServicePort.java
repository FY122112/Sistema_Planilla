package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.MovimientoPlanilla;

import java.util.List;

public interface MovimientoPlanillaServicePort {

    List<MovimientoPlanilla> findAll();

    MovimientoPlanilla findById(Long id);

    MovimientoPlanilla update(Long id, MovimientoPlanilla movimientoPlanilla);

    MovimientoPlanilla save(MovimientoPlanilla movimientoPlanilla);

    void deleteById(Long id);

}
