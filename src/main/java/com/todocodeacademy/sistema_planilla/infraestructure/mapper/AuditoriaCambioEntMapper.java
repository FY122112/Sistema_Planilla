package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.AuditoriaCambio;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.AuditoriaCambioEntity;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaCambioEntMapper {

    public AuditoriaCambio toDomain(AuditoriaCambioEntity entity) {

        if (entity == null) {
            return null;
        }

        return AuditoriaCambio.reconstruir(
                entity.getIdAuditoria(),
                entity.getUsuario(),
                entity.getIdPlanilla(),
                entity.getIdDetalle(),
                entity.getMontoAnterior(),
                entity.getMontoNuevo(),
                entity.getFecha()
        );
    }

    public AuditoriaCambioEntity toEntity(AuditoriaCambio domain) {

        if (domain == null) {
            return null;
        }

        AuditoriaCambioEntity entity = new AuditoriaCambioEntity();

        entity.setIdAuditoria(domain.getIdAuditoria());
        entity.setUsuario(domain.getUsuario());
        entity.setIdPlanilla(domain.getIdPlanilla());
        entity.setIdDetalle(domain.getIdDetalle());
        entity.setMontoAnterior(domain.getMontoAnterior());
        entity.setMontoNuevo(domain.getMontoNuevo());
        entity.setFecha(domain.getFecha());

        return entity;
    }
}
