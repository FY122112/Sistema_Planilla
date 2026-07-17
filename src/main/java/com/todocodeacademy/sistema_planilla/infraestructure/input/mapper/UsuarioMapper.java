package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpleadoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpresaRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.RoleRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.domain.model.Empresa;
import com.todocodeacademy.sistema_planilla.domain.model.Role;
import com.todocodeacademy.sistema_planilla.domain.model.UsuarioSec;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.CreateUsuarioRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.UpdateUsuarioRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {

    private final RoleRepositoryPort roleRepository;
    private final EmpleadoRepositoryPort empleadoRepository;
    private final EmpresaRepositoryPort empresaRepository;

    public UsuarioSec toDomain(CreateUsuarioRequest request) {

        UsuarioSec usuario = UsuarioSec.crearNuevoUsuario(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()
        );

        Set<Long> roleIds = request.getRoleIds();

        if (roleIds != null) {
            for (Long roleId : roleIds) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Rol no encontrado: " + roleId));

                usuario.agregarRol(role);
            }
        }

        if (request.getEmpleadoId() != null) {
            Empleado empleado = empleadoRepository.findById(request.getEmpleadoId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Empleado no encontrado: " + request.getEmpleadoId()));

            usuario.asignarEmpleado(empleado);
        }

        if (request.getEmpresaId() != null) {
            Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Empresa no encontrada: " + request.getEmpresaId()));

            usuario.asignarEmpresa(empresa);
        }

        return usuario;
    }

    public UsuarioSec toDomain(UpdateUsuarioRequest request) {

        Empleado empleado = null;
        if (request.getEmpleadoId() != null) {
            empleado = empleadoRepository.findById(request.getEmpleadoId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Empleado no encontrado: " + request.getEmpleadoId()));
        }

        Set<Role> roles = null;
        if (request.getRoleIds() != null) {
            roles = request.getRoleIds().stream()
                    .map(roleId -> roleRepository.findById(roleId)
                            .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + roleId)))
                    .collect(Collectors.toSet());
        }

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
                empleado,
                null,
                null,
                null,
                roles
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
