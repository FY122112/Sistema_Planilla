package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.PuestoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Puesto;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.PuestoEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaPuestoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@Repository
@RequiredArgsConstructor
public class PuestoRepositotyAdapter implements PuestoRepositoryPort {

    private final JpaPuestoRepository repository;;

    private final PuestoEntMapper mapper;

    @Override
    public List<Puesto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Puesto> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Puesto save(Puesto puesto) {
        return mapper.toDomain(repository.save(mapper.toEntity(puesto)));
    }

    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);

    }

    @Override
    public Optional<Puesto> findByNombrePuesto(String name) {
        return repository.findByNombre(name).map(mapper::toDomain);
    }

    @Override
    public List<Puesto> findBySalarioBaseGreaterThanEqual(BigDecimal salarioBase) {
        return repository.findBySalarioBaseGreaterThanEqual(salarioBase)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

}
