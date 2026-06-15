package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUsuarioRequest {

    private String username;

    private String password;

    private String email;

    private Boolean enabled;

}
