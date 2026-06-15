package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.PermissionRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Permission;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.PermissionEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaPermisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PermissionRepositoryAdapter implements PermissionRepositoryPort {

    private final JpaPermisoRepository repository;

    private final PermissionEntMapper mapper;

    @Override
    public List<Permission> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Permission save(Permission permission) {
        return mapper.toDomain(repository.save(mapper.toEntity(permission)));
    }

    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);

    }

    @Override
    public Optional<Permission> findByNombre(String nombre) {
        return repository.findByName(nombre).map(mapper::toDomain);
    }

}
