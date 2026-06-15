package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.Role;

import java.util.List;

public interface RoleServicePort {

    List<Role> findAll();

    Role findById(Long id);

    Role update(Role role,Long id);

    Role save(Role role);

    void deleteById(Long id);

    Role findByNombre(String nombre);

}
