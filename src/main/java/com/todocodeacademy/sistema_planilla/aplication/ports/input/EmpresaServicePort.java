package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.Empresa;

import java.util.List;

public interface EmpresaServicePort {

    List<Empresa> findAll();

    Empresa findById(Long id);

    Empresa save(Empresa empresa);

    Empresa update(Long id, Empresa empresa);

    void deleteById(Long id);

    Empresa buscarPorRuc(String ruc);

    Empresa unicaEmpresa();
}
