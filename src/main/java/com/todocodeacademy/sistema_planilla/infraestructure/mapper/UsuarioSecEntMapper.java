package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Role;
import com.todocodeacademy.sistema_planilla.domain.model.UsuarioSec;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UsuarioSecEntMapper {

    private final RoleEntMapper roleMapper;
    private final EmpleadoEntMapper empleadoMapper;
    private final EmpresaEntMapper empresaMapper;

    // =========================
    // ENTITY → DOMAIN
    // =========================
    public UsuarioSec toDomain(UsuarioSecEntity entity) {

        if (entity == null) return null;

        return UsuarioSec.reconstruir(
                entity.getIdUsuario(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail(),
                entity.isEnabled(),
                entity.isAccountNonExpired(),
                entity.isAccountNonLocked(),
                entity.isCredentialsNonExpired(),
                entity.getLastLogin(),

                // 🔥 ahora objetos, no IDs
                empleadoMapper.toDomain(entity.getEmpleado()),
                empresaMapper.toDomain(entity.getEmpresa()),

                entity.getCreatedAt(),
                entity.getUpdatedAt(),

                mapRolesToDomain(entity.getRoles())
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public UsuarioSecEntity toEntity(UsuarioSec domain) {

        if (domain == null) return null;

        UsuarioSecEntity entity = new UsuarioSecEntity();

        entity.setIdUsuario(domain.getIdUsuario());
        entity.setUsername(domain.getUsername());
        entity.setPassword(domain.getPassword());
        entity.setEmail(domain.getEmail());

        entity.setEnabled(domain.isEnabled());
        entity.setAccountNonExpired(domain.isAccountNonExpired());
        entity.setAccountNonLocked(domain.isAccountNonLocked());
        entity.setCredentialsNonExpired(domain.isCredentialsNonExpired());

        entity.setLastLogin(domain.getLastLogin());

        // 🔥 relaciones completas (correcto en DDD)
        entity.setEmpleado(empleadoMapper.toEntity(domain.getEmpleado()));
        entity.setEmpresa(empresaMapper.toEntity(domain.getEmpresa()));

        entity.setRoles(mapRolesToEntity(domain.getRoles()));

        return entity;
    }

    // =========================
    // 🔄 ROLES
    // =========================
    private Set<Role> mapRolesToDomain(Set<RoleEntity> roles) {
        if (roles == null) return Set.of();

        return roles.stream()
                .map(roleMapper::toDomain)
                .collect(Collectors.toSet());
    }

    private Set<RoleEntity> mapRolesToEntity(Set<Role> roles) {
        if (roles == null) return Set.of();

        return roles.stream()
                .map(roleMapper::toEntity)
                .collect(Collectors.toSet());
    }
}