package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.HistorialPuesto;

import java.util.List;
import java.util.Optional;

public interface HistorialPuestoRepositoryPort {

    List<HistorialPuesto> findAll();

    Optional<HistorialPuesto> findById(Long id);

    HistorialPuesto save(HistorialPuesto banco);

    void deleteById(Long id);

}
