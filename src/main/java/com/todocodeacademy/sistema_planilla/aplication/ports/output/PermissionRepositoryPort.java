package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionRepositoryPort {

    List<Permission> findAll();

    Optional<Permission> findById(Long id);

    Permission save(Permission permission);

    void deleteById(Long id);

    Optional<Permission> findByNombre(String nombre);
}
