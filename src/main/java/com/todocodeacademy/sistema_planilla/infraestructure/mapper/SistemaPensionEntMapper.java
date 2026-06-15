package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.SistemaPension;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.SistemaPensionEntity;
import org.springframework.stereotype.Component;

@Component
public class SistemaPensionEntMapper {

    // =========================
    // ENTITY → DOMAIN
    // =========================
    public SistemaPension toDomain(SistemaPensionEntity entity) {

        if (entity == null) return null;

        return SistemaPension.reconstruir(
                entity.getIdSistema(),
                entity.getNombre(),
                entity.getTipo(),
                entity.getPorcentajeAporte(),
                entity.getPorcentajeComision(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public SistemaPensionEntity toEntity(SistemaPension domain) {

        if (domain == null) return null;

        SistemaPensionEntity entity = new SistemaPensionEntity();

        entity.setIdSistema(domain.getIdSistema());
        entity.setNombre(domain.getNombre());
        entity.setTipo(domain.getTipo());
        entity.setPorcentajeAporte(domain.getPorcentajeAporte());
        entity.setPorcentajeComision(domain.getPorcentajeComision());

        return entity;
    }
}