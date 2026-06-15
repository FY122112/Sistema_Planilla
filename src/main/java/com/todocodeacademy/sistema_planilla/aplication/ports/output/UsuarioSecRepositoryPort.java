package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.SistemaPension;
import com.todocodeacademy.sistema_planilla.domain.model.UsuarioSec;

import java.util.List;
import java.util.Optional;

public interface UsuarioSecRepositoryPort {

    List<UsuarioSec> findAll();

    Optional<UsuarioSec> findById(Long id);

    UsuarioSec save(UsuarioSec banco);

    void deleteById(Long id);

    Optional<UsuarioSec> findByUsername(String nombre);

    Optional<UsuarioSec> findByEmail(String email);

}
