package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.HistorialPuestoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.HistorialPuesto;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.HistorialPuestoEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaHistorialPuestoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HistorialPuestoRepositoryAdapter implements HistorialPuestoRepositoryPort {

    private final JpaHistorialPuestoRepository repository;

    private final HistorialPuestoEntMapper mapper;

    @Override
    public List<HistorialPuesto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<HistorialPuesto> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public HistorialPuesto save(HistorialPuesto banco) {
        return mapper.toDomain(repository.save(mapper.toEntity(banco)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
