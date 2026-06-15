package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class RoleResponseDTO {

    private Long idRole;

    private String name;

    private String description;

    private Set<PermissionResponseDTO> permissions;

}
