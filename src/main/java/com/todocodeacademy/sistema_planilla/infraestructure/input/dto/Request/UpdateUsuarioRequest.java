package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateUsuarioRequest {

    private String username;

    private String password;

    private String email;

    private Boolean enabled;

    private Long empleadoId;

    // null = no tocar los roles actuales; cualquier otro valor (incluyendo vacío) reemplaza
    // el conjunto de roles por completo.
    private Set<Long> roleIds;

}
