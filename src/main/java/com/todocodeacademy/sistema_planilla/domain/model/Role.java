package com.todocodeacademy.sistema_planilla.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {

    Long idRole;
    String name;
    String description;

    Set<Permission> permissions;

    Instant createdAt;
    Instant updatedAt;

    // =========================
    // 🏗️ CONSTRUCTOR
    // =========================
    public Role(String name, String description) {
        this.name = name;
        this.description = description;
        this.permissions = new HashSet<>();
    }

    // =========================
    // 🔄 RECONSTRUCCIÓN
    // =========================
    public static Role reconstruir(
            Long id,
            String name,
            String description,
            Set<Permission> permissions,
            Instant createdAt,
            Instant updatedAt
    ) {
        Role r = new Role(name, description);
        r.idRole = id;
        r.permissions = permissions != null ? permissions : new HashSet<>();
        r.createdAt = createdAt;
        r.updatedAt = updatedAt;
        return r;
    }

    // =========================
    // 🧠 LÓGICA
    // =========================

    public void actualizarNombre(String nombre){

        this.name = nombre;

    }

    public void actualizarDescription(String description){
        this.description = description;
    }
    public void agregarPermiso(Permission permission) {
        this.permissions.add(permission);
    }

    public void removerPermiso(Permission permission) {
        this.permissions.remove(permission);
    }

    public boolean tienePermiso(String permiso) {
        return permissions.stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(permiso));
    }
}