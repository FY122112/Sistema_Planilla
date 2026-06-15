package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.RoleServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.RoleRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements RoleServicePort {

    private final RoleRepositoryPort  roleServ;

    @Override
    public List<Role> findAll() {
        return roleServ.findAll();
    }

    @Override
    public Role findById(Long id) {
        return roleServ.findById(id).orElseThrow(()-> new IllegalArgumentException("Role no encontrado with id " + id));
    }

    @Override
    public Role update(Role role,Long id) {
        Role update = roleServ.findById(id).orElseThrow(()-> new IllegalArgumentException("Role no encontrado with id " + id));

        if (role.getName() != null) {
            update.actualizarNombre(role.getName());
        }

        if (role.getDescription() != null) {
            update.actualizarDescription(role.getDescription());
        }

        update =  roleServ.save(update);
        return update;

    }

    @Override
    public Role save(Role role) {
        roleServ.findByNombre(role.getName())
                .ifPresent(r -> {
                    throw new IllegalArgumentException(
                            "Ya existe un rol con ese nombre");
                });

        return roleServ.save(role);
    }

    @Override
    public void deleteById(Long id) {
        Role role = roleServ.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("El rol no existe"));

        roleServ.deleteById(id);

    }

    @Override
    public Role findByNombre(String nombre) {
        Role rol = roleServ.findByNombre(nombre).orElseThrow(()-> new IllegalArgumentException("Role no encontrado with name " + nombre));

        return rol;
    }
}
