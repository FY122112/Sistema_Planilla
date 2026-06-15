package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionResponseDTO {

    private Long idPermission;

    private String name;

    private String description;

}
