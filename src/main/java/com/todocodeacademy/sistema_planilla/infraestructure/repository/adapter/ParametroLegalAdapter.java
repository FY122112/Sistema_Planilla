package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.ParametroLegalRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.ParametroLegal;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.ParametroLegalEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.ParametroLegalEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaParametroLegalRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ParametroLegalAdapter implements ParametroLegalRepositoryPort {

    private final JpaParametroLegalRepository repository;

    private final ParametroLegalEntMapper mapper;

    @Override
    public List<ParametroLegal> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ParametroLegal> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public ParametroLegal save(ParametroLegal parametroLegal) {
        return mapper.toDomain(repository.save(mapper.toEntity(parametroLegal)));
    }

    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);
    }

    @Override
    public Optional<ParametroLegal> findVigenteByCodigoAndFecha(String codigo, LocalDate fecha) {
        return Optional.empty();
    }

    @Override
    public Optional<ParametroLegal> findTopByCodigoOrderByFechaInicioVigenciaDesc(String codigo) {
        return Optional.empty();
    }

    @Override
    public List<ParametroLegal> findByCodigo(String codigo) {
        return repository.findByCodigo(codigo)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<ParametroLegal> findByDescripcionContainingIgnoreCase(String descripcion) {
        return repository.findByDescripcionContainingIgnoreCase(descripcion)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<ParametroLegal> findByFechaInicioVigenciaBetween(LocalDate startDate, LocalDate endDate) {
        return repository.findByFechaInicioVigenciaBetween(startDate, endDate)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }


}
