package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.AuditoriaCambio;

import java.util.List;

public interface AuditoriaCambioRepositoryPort {

    AuditoriaCambio save(AuditoriaCambio auditoria);

    List<AuditoriaCambio> findByIdPlanilla(Long idPlanilla);
}
