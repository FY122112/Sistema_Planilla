package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.PlanillaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoPlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Planilla;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.PlanillaEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaPlanillaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlanillaRepositoryAdapter implements PlanillaRepositoryPort {

    private final JpaPlanillaRepository repository;

    private final PlanillaEntMapper mapper;

    @Override
    public List<Planilla> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Planilla> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Planilla save(Planilla banco) {
        return mapper.toDomain(repository.save(mapper.toEntity(banco)));
    }

    @Override
    public void deleteById(Long id) {
            repository.deleteById(id);
    }

    @Override
    public Optional<Planilla> findByMesAndAnioAndTipoPlanilla(Integer mes, Integer anio, TipoPlanilla tipoPlanilla) {
        return repository.findByMesAndAnioAndTipoPlanilla(mes,anio,tipoPlanilla).map(mapper::toDomain);
    }

    @Override
    public List<Planilla> findByFechaGeneradaBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return repository.findByFechaGeneradaBetween(fechaInicio,fechaFin)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Planilla> findByAnioAndTipoPlanilla(Integer anio, TipoPlanilla tipoPlanilla) {
        return repository.findByAnioAndTipoPlanilla(anio,tipoPlanilla)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Planilla> findByMesAndAnio(Integer mes, Integer anio) {
        return repository.findByMesAndAnio(mes,anio).map(mapper::toDomain);
    }
}
