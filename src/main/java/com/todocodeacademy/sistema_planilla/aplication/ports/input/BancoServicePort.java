package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.Banco;

import java.util.List;

public interface BancoServicePort {

    List<Banco> findAll();

    Banco findById(Long id);

    Banco save(Banco banco);

    Banco update(Long id, Banco banco);

    void deleteById(Long id);

}
