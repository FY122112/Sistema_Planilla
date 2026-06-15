package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Permission;
import com.todocodeacademy.sistema_planilla.domain.model.Role;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.RoleRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.RoleResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final PermissionMapper mapper;

    public Role toDomain(RoleRequestDTO dto, Set<Permission> permissions) {

        return Role.reconstruir(
                null,
                dto.getName(),
                dto.getDescription(),
                permissions,
                null,
                null
        );
    }

    public RoleResponseDTO toResponse(Role role) {

        return RoleResponseDTO.builder()
                .idRole(role.getIdRole())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(
                        role.getPermissions() == null
                                ? Set.of()
                                : role.getPermissions()
                                .stream()
                                .map(mapper::toResponse)
                                .collect(Collectors.toSet())
                )
                .build();
    }
}