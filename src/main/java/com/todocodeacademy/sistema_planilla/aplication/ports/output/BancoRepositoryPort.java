package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.Banco;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.BancoEntity;

import java.util.List;
import java.util.Optional;

public interface BancoRepositoryPort {

    List<Banco> findAll();

    Optional<Banco> findById(Long id);

    Optional<Banco> getBancoByNombreBanco(String nombreBanco);

    Optional<Banco> getBancoByCodigoBanco(String codigoBanco);

    Banco save(Banco banco);

    void deleteById(Long id);

}
