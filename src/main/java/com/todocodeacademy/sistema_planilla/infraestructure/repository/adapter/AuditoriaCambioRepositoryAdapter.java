package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.AuditoriaCambioRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.AuditoriaCambio;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.AuditoriaCambioEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaAuditoriaCambioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuditoriaCambioRepositoryAdapter implements AuditoriaCambioRepositoryPort {

    private final JpaAuditoriaCambioRepository repository;
    private final AuditoriaCambioEntMapper mapper;

    @Override
    public AuditoriaCambio save(AuditoriaCambio auditoria) {
        return mapper.toDomain(repository.save(mapper.toEntity(auditoria)));
    }

    @Override
    public List<AuditoriaCambio> findByIdPlanilla(Long idPlanilla) {
        return repository.findByIdPlanillaOrderByFechaDesc(idPlanilla)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
