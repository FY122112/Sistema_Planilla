package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.SistemaPension;

import java.util.List;
import java.util.Optional;

public interface SistemaPensionRepositoryPort {

    List<SistemaPension> findAll();

    Optional<SistemaPension> findById(Long id);

    SistemaPension save(SistemaPension banco);

    void deleteById(Long id);

}
