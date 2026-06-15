package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.ConceptoPagoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.ConceptoPago;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.ConceptoPagoEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaConceptoPagoRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ConceptoPagoRespositoryAdapter implements ConceptoPagoRepositoryPort {

    private final JpaConceptoPagoRepository repository;

    private final ConceptoPagoEntMapper mapper;

    @Override
    public List<ConceptoPago> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ConceptoPago> findById(Long id) {
        return  repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public ConceptoPago save(ConceptoPago banco) {
        return mapper.toDomain(repository.save(mapper.toEntity(banco)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<ConceptoPago> findByNombreConcepto(String nombreConcepto) {
        return repository.findByNombreConcepto(nombreConcepto).map(mapper::toDomain);
    }

    @Override
    public Optional<ConceptoPago> findByNombreConceptoAndTipo(String nombreConcepto, TipoConcepto tipo) {
        return repository.findByNombreConceptoAndTipo(nombreConcepto, tipo).map(mapper::toDomain);
    }

    @Override
    public List<ConceptoPago> findByTipo(TipoConcepto tipo) {
        return repository.findByTipo(tipo).stream().map(concepto -> mapper.toDomain(concepto)).toList();

    }

    @Override
    public List<ConceptoPago> findByAfectoOnpOrAfectoAfpOrAfectoEssalud(Boolean afectoOnp, Boolean afectoAfp, Boolean afectoEssalud) {
        return List.of();
    }
}
