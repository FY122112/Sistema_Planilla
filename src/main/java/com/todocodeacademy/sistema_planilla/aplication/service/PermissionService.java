package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.PermissionServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.PermissionRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService implements PermissionServicePort {

    private final PermissionRepositoryPort perRepo;

    @Override
    public List<Permission> findAll() {
        return perRepo.findAll();
    }

    @Override
    public Permission findById(Long id) {
        return perRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("No existe el permission en la base de datos"));
    }

    @Override
    public Permission save(Permission permission) {
        return perRepo.save(permission);
    }

    @Override
    public Permission update(Long id, Permission permission) {

        Permission update = perRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("No existe el permission en la base de datos"));

        if (permission.getName() != null){
            update.actualizarName(permission.getName());
        }
        if (permission.getDescription() != null){
            update.actualizarDescripcion(permission.getDescription());
        }

        return perRepo.save(permission);

    }

    @Override
    public void deleteById(Long id) {
            perRepo.deleteById(id);
    }

    @Override
    public Permission findByNombre(String nombre) {
        return perRepo.findByNombre(nombre).orElseThrow(()-> new IllegalArgumentException("No existe este nombre de permiso en la base de datos"));
    }

}
