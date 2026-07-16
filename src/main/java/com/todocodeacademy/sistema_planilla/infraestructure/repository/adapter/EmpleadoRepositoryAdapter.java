package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpleadoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.EmpleadoEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaEmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmpleadoRepositoryAdapter implements EmpleadoRepositoryPort {

    private final JpaEmpleadoRepository repository;
    private final EmpleadoEntMapper mapper;

    @Override
    public List<Empleado> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Empleado> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Empleado> findByNumeroDocumento(String numeroDocumento) {
        return repository.findByNumeroDocumentoAndEliminadoFalse(numeroDocumento)
                .map(mapper::toDomain);
    }

    @Override
    public Empleado save(Empleado empleado) {

        var entity = mapper.toEntity(empleado);

        var savedEntity = repository.save(entity);

        // volver a consultar ya persistido completamente
        var empleadoCompleto = repository.findById(
                savedEntity.getIdEmpleado()
        ).orElseThrow(() ->
                new RuntimeException("Error al recuperar empleado")
        );

        return mapper.toDomain(empleadoCompleto);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Empleado> findByEstado(Boolean estado) {
        return repository.findByEstado(estado)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Empleado> findByTieneHijosCalificados(Boolean tieneHijosCalificados) {
        return repository.findByTieneHijosCalificados(tieneHijosCalificados)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Empleado> searchByDniOrNameOrLastName(String query) {
        return repository.searchByDniOrNameOrLastName(query)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}