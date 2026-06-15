package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.UsuarioSec;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.CreateUsuarioRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.UpdateUsuarioRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.UsuarioResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UsuarioMapper {
    public UsuarioSec toDomain(CreateUsuarioRequest request) {

        return UsuarioSec.crearNuevoUsuario(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()
        );
    }

    public UsuarioSec toDomain(UpdateUsuarioRequest request) {

        return UsuarioSec.reconstruir(
                null,
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getEnabled() != null ? request.getEnabled() : true,
                true,
                true,
                true,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public UsuarioResponse toResponse(UsuarioSec usuario) {

        return UsuarioResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .enabled(usuario.isEnabled())
                .accountNonExpired(usuario.isAccountNonExpired())
                .accountNonLocked(usuario.isAccountNonLocked())
                .credentialsNonExpired(usuario.isCredentialsNonExpired())
                .lastLogin(usuario.getLastLogin())
                .empleadoId(
                        usuario.getEmpleado() != null
                                ? usuario.getEmpleado().getIdEmpleado()
                                : null
                )
                .empresaId(
                        usuario.getEmpresa() != null
                                ? usuario.getEmpresa().getIdEmpresa()
                                : null
                )
                .roles(
                        usuario.getRoles()
                                .stream()
                                .map(role -> role.getName())
                                .collect(Collectors.toSet())
                )
                .createdAt(usuario.getCreatedAt())
                .updatedAt(usuario.getUpdatedAt())
                .build();
    }
}
