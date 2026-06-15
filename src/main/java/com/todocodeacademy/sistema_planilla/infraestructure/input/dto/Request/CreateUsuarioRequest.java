package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CreateUsuarioRequest {

    private String username;

    private String password;

    private String email;

    private Long empleadoId;

    private Long empresaId;

    private Set<Long> roleIds;
}