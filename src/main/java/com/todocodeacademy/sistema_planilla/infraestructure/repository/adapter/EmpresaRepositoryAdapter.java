package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpresaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Empresa;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.EmpresaEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaEmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmpresaRepositoryAdapter implements EmpresaRepositoryPort {

    private final JpaEmpresaRepository repository;
    private final EmpresaEntMapper mapper;


    @Override
    public List<Empresa> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Empresa> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Empresa save(Empresa banco) {
        return mapper.toDomain(repository.save(mapper.toEntity(banco)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Empresa> findByRuc(String ruc) {
        return repository.findByRuc(ruc).map(mapper::toDomain);
    }
}
