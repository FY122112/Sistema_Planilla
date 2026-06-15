package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Set;

@Getter
@Builder
public class UsuarioResponse {

    private Long idUsuario;

    private String username;

    private String email;

    private Boolean enabled;

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;

    private Instant lastLogin;

    private Long empleadoId;

    private Long empresaId;

    private Set<String> roles;

    private Instant createdAt;

    private Instant updatedAt;
}