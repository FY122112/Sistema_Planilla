package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Permission;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.PermissionEntity;
import org.springframework.stereotype.Component;

@Component
public class PermissionEntMapper {

    // =========================
    // ENTITY → DOMAIN
    // =========================
    public Permission toDomain(PermissionEntity entity) {

        if (entity == null) return null;

        return Permission.reconstruir(
                entity.getIdPermission(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public PermissionEntity toEntity(Permission domain) {

        if (domain == null) return null;

        PermissionEntity entity = new PermissionEntity();

        entity.setIdPermission(domain.getIdPermission());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());

        return entity;
    }
}