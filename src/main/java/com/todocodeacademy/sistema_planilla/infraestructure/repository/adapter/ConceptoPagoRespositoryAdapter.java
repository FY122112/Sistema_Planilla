package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.ConceptoPagoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.ConceptoPago;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.ConceptoPagoEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaConceptoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConceptoPagoRespositoryAdapter
        implements ConceptoPagoRepositoryPort {

    private final JpaConceptoPagoRepository repository;
    private final ConceptoPagoEntMapper mapper;

    @Override
    public List<ConceptoPago> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ConceptoPago> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public ConceptoPago save(ConceptoPago concepto) {
        return mapper.toDomain(
                repository.save(
                        mapper.toEntity(concepto)
                )
        );
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<ConceptoPago> findByNombreConcepto(
            String nombreConcepto
    ) {
        return repository
                .findByNombreConcepto(nombreConcepto)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<ConceptoPago> findByNombreConceptoAndTipo(
            String nombreConcepto,
            TipoConcepto tipo
    ) {
        return repository
                .findByNombreConceptoAndTipoConcepto(
                        nombreConcepto,
                        tipo
                )
                .map(mapper::toDomain);
    }

    @Override
    public List<ConceptoPago> findByTipo(
            TipoConcepto tipo
    ) {
        return repository
                .findByTipoConcepto(tipo)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<ConceptoPago> findByAfectoEssalud(
            Boolean afectoEssalud
    ) {
        return repository
                .findByAfectoEssalud(afectoEssalud)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}