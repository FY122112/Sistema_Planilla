package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.DetallePlanillaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.DetallePlanillaEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaDetallePlanillaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DetallePlanillaRepositoryAdapter implements DetallePlanillaRepositoryPort {

    private final JpaDetallePlanillaRepository repository;
    private final DetallePlanillaEntMapper mapper;

    @Override
    public List<DetallePlanilla> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<DetallePlanilla> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public DetallePlanilla save(DetallePlanilla banco) {
        return mapper.toDomain(repository.save(mapper.toEntity(banco)));
    }

    @Override
    public void deleteById(Long id) {
            repository.deleteById(id);
    }
}
