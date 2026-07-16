package com.todocodeacademy.sistema_planilla.infraestructure.security;

// Implementa la interfaz de Spring Security del mismo nombre para que
// Authentication#getName() devuelva el username en vez de caer al toString() por defecto
// del record (que expondría "AuthenticatedPrincipal[username=..., idEmpleado=...]").
public record AuthenticatedPrincipal(String username, Long idEmpleado)
        implements org.springframework.security.core.AuthenticatedPrincipal {

    @Override
    public String getName() {
        return username;
    }
}
