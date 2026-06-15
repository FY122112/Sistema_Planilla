package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Permission;
import com.todocodeacademy.sistema_planilla.domain.model.Role;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.PermissionEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleEntMapper {

    private final PermissionEntMapper permissionMapper;

    // =========================
    // ENTITY → DOMAIN
    // =========================
    public Role toDomain(RoleEntity entity) {

        if (entity == null) return null;

        Set<Permission> permissions = entity.getPermissions() != null
                ? entity.getPermissions()
                .stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toSet())
                : null;

        return Role.reconstruir(
                entity.getIdRole(),
                entity.getName(),
                entity.getDescription(),
                permissions,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public RoleEntity toEntity(Role domain) {

        if (domain == null) return null;

        RoleEntity entity = new RoleEntity();

        entity.setIdRole(domain.getIdRole());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());

        if (domain.getPermissions() != null) {
            Set<PermissionEntity> permissions = domain.getPermissions()
                    .stream()
                    .map(permissionMapper::toEntity)
                    .collect(Collectors.toSet());

            entity.setPermissions(permissions);
        }

        return entity;
    }
}