package com.todocodeacademy.sistema_planilla.domain.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission {

    Long idPermission;
    String name;
    String description;

    Instant createdAt;
    Instant updatedAt;

    // =========================
    // 🏗️ CONSTRUCTOR
    // =========================
    public Permission(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // =========================
    // 🔄 RECONSTRUCCIÓN
    // =========================
    public static Permission reconstruir(
            Long id,
            String name,
            String description,
            Instant createdAt,
            Instant updatedAt
    ) {
        Permission p = new Permission(name, description);
        p.idPermission = id;
        p.createdAt = createdAt;
        p.updatedAt = updatedAt;
        return p;
    }

    // =========================
    // 🧠 LÓGICA
    // =========================

    public void actualizarName(String newName) {
        /*if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Descripción inválida");
        }*/
        this.description = newName;
    }

    public void actualizarDescripcion(String descripcion) {
       /* if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("Descripción inválida");
        }*/
        this.description = descripcion;
    }
}