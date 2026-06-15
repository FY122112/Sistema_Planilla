package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.HistorialPuesto;

import java.util.List;

public interface HistorialPuestoServicePort {

    List<HistorialPuesto> findAll();

    HistorialPuesto findById(Long id);

    HistorialPuesto update(Long id, HistorialPuesto historialPuesto);

    HistorialPuesto save(HistorialPuesto historialPuesto);

    void deleteById(Long id);

}
