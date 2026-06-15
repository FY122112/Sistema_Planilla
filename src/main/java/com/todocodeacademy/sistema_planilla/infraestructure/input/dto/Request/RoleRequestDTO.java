package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;
@Data
public class RoleRequestDTO {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String description;

    @NotEmpty
    private Set<Long> permissionIds;

}
