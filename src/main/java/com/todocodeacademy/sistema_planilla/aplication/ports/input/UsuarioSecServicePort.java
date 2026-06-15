package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.UsuarioSec;

import java.util.List;

public interface UsuarioSecServicePort {

    List<UsuarioSec> findAll();

    UsuarioSec findById(Long id);

    UsuarioSec save(UsuarioSec usuarioSec);

    UsuarioSec update(Long id, UsuarioSec usuarioSec);

    void deleteById(Long id);

    UsuarioSec findByUsername(String username);

    UsuarioSec findByEmail(String email);

}
