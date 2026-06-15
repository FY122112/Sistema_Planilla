package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.Permission;

import java.util.List;

public interface PermissionServicePort {

    List<Permission> findAll();

    Permission findById(Long id);

    Permission save(Permission permission);

    Permission update(Long id,Permission permission);

    void deleteById(Long id);

    Permission findByNombre(String nombre);

}
