package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUsuarioRequest {

    private String username;

    private String password;

    private String email;

    private Long empleadoId;

    private Long empresaId;

}
