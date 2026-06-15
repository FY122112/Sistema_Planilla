package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.domain.model.Empresa;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepositoryPort {

    List<Empresa> findAll();

    Optional<Empresa> findById(Long id);

    Empresa save(Empresa banco);

    void deleteById(Long id);

    Optional<Empresa> findByRuc(String ruc);
}
