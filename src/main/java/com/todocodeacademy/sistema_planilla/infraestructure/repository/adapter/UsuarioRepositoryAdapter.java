package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.UsuarioSecRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.UsuarioSec;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.UsuarioSecEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioSecRepositoryPort {

    private final JpaUsuarioRepository repository;

    private final UsuarioSecEntMapper mapper;


    @Override
    public List<UsuarioSec> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<UsuarioSec> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public UsuarioSec save(UsuarioSec banco) {
        return mapper.toDomain(repository.save(mapper.toEntity(banco)));
    }

    @Override
    public void deleteById(Long id) {
            repository.deleteById(id);
    }

    @Override
    public Optional<UsuarioSec> findByUsername(String nombre) {
        return repository.findByUsername(nombre).map(mapper::toDomain);

    }

    @Override
    public Optional<UsuarioSec> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toDomain);
    }
}
