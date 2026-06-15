package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.RoleRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Role;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.RoleEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final JpaRoleRepository repository;

    private final RoleEntMapper mapper;


    @Override
    public List<Role> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Role save(Role role) {
        return mapper.toDomain(repository.save(mapper.toEntity(role)));
    }

    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);

    }

    @Override
    public Optional<Role> findByNombre(String name) {
        return repository.findByName(name).map(mapper::toDomain);
    }

}
