package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.BancoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Banco;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.BancoEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaBancoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BancoRepositoryAdapter implements BancoRepositoryPort {

    private final JpaBancoRepository repository;

    private final BancoEntMapper mapper;


    @Override
    public List<Banco> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Banco> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Banco> getBancoByNombreBanco(String nombreBanco) {
        return repository.findByNombreBanco(nombreBanco).map(mapper::toDomain);
    }

    @Override
    public Optional<Banco> getBancoByCodigoBanco(String codigoBanco) {
        return repository.findByCodigoBanco(codigoBanco).map(mapper::toDomain);
    }

    @Override
    public Banco save(Banco banco) {
        return mapper.toDomain(repository.save(mapper.toEntity(banco)));
    }

    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);

    }
}
