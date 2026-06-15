package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.SistemaPensionRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.SistemaPension;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.SistemaPensionEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaSistemaPension;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SistemaPensionRepositoryAdapter implements SistemaPensionRepositoryPort {

    private final JpaSistemaPension repository;

    private final SistemaPensionEntMapper mapper;


    @Override
    public List<SistemaPension> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<SistemaPension> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public SistemaPension save(SistemaPension banco) {
        return mapper.toDomain(repository.save(mapper.toEntity(banco)));
    }

    @Override
    public void deleteById(Long id) {
            repository.deleteById(id);
    }
}
