package com.todocodeacademy.sistema_planilla.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsuarioSec {

    Long idUsuario;

    String username;
    String password;
    String email;

    boolean enabled;
    boolean accountNonExpired;
    boolean accountNonLocked;
    boolean credentialsNonExpired;

    Instant lastLogin;

    Set<Role> roles = new HashSet<>();

    Empleado empleado;
    Empresa empresa;

    Instant createdAt;
    Instant updatedAt;

    private UsuarioSec() {
    }

    // =========================================
    // CREACIÓN
    // =========================================

    public static UsuarioSec crearNuevoUsuario(
            String username,
            String password,
            String email
    ) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username obligatorio");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password obligatorio");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email obligatorio");
        }

        UsuarioSec usuario = new UsuarioSec();

        usuario.username = username;
        usuario.password = password;
        usuario.email = email;

        usuario.enabled = true;
        usuario.accountNonExpired = true;
        usuario.accountNonLocked = true;
        usuario.credentialsNonExpired = true;

        return usuario;
    }

    // =========================================
    // RECONSTRUCCIÓN
    // =========================================

    public static UsuarioSec reconstruir(
            Long idUsuario,
            String username,
            String password,
            String email,
            boolean enabled,
            boolean accountNonExpired,
            boolean accountNonLocked,
            boolean credentialsNonExpired,
            Instant lastLogin,
            Empleado empleado,
            Empresa empresa,
            Instant createdAt,
            Instant updatedAt,
            Set<Role> roles
    ) {

        UsuarioSec usuario = new UsuarioSec();

        usuario.idUsuario = idUsuario;
        usuario.username = username;
        usuario.password = password;
        usuario.email = email;

        usuario.enabled = enabled;
        usuario.accountNonExpired = accountNonExpired;
        usuario.accountNonLocked = accountNonLocked;
        usuario.credentialsNonExpired = credentialsNonExpired;

        usuario.lastLogin = lastLogin;

        usuario.empleado = empleado;
        usuario.empresa = empresa;

        usuario.createdAt = createdAt;
        usuario.updatedAt = updatedAt;

        if (roles != null) {
            usuario.roles = roles;
        }

        return usuario;
    }

    // =========================================
    // ACTUALIZACIONES
    // =========================================

    public void asignarContraseña(String password) {
        this.password = password;
    }

    public void actualizarUsername(String username) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username obligatorio");
        }

        this.username = username;
    }

    public void actualizarEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email obligatorio");
        }

        this.email = email;
    }

    public void actualizarPassword(String password) {

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password obligatorio");
        }

        this.password = password;
    }

    public void habilitar() {
        this.enabled = true;
    }

    public void deshabilitar() {
        this.enabled = false;
    }

    public void registrarLogin() {
        this.lastLogin = Instant.now();
    }

    // =========================================
    // RELACIONES
    // =========================================

    public void asignarEmpleado(Empleado empleado) {

        if (empleado == null) {
            throw new IllegalArgumentException("Empleado obligatorio");
        }

        this.empleado = empleado;
    }

    public void asignarEmpresa(Empresa empresa) {

        if (empresa == null) {
            throw new IllegalArgumentException("Empresa obligatoria");
        }

        this.empresa = empresa;
    }

    // =========================================
    // ROLES
    // =========================================

    public void agregarRol(Role rol) {

        if (rol == null) {
            throw new IllegalArgumentException("Rol obligatorio");
        }

        this.roles.add(rol);
    }

    public void removerRol(Role rol) {

        if (rol == null) {
            return;
        }

        this.roles.remove(rol);
    }

    public void actualizarRoles(Set<Role> roles) {

        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("El usuario debe tener al menos un rol");
        }

        this.roles = roles;
    }
}