package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.MovimientoPlanillaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.MovimientoPlanilla;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.MovimientoPlanillaEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaMovimientoPlanillaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MovimientoPlanillaRepositoryAdapter implements MovimientoPlanillaRepositoryPort {

    private final JpaMovimientoPlanillaRepository repository;

    private final MovimientoPlanillaEntMapper mapper;


    @Override
    public List<MovimientoPlanilla> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<MovimientoPlanilla> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public MovimientoPlanilla save(MovimientoPlanilla banco) {
        return mapper.toDomain(repository.save(mapper.toEntity(banco)));
    }

    @Override
    public void deleteById(Long id) {
            repository.deleteById(id);
    }
}
