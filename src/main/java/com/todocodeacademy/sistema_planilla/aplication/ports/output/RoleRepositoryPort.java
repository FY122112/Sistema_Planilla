package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepositoryPort {

    List<Role> findAll();

    Optional<Role> findById(Long id);

    Role save(Role role);

    void deleteById(Long id);

    Optional<Role> findByNombre(String name);

}
