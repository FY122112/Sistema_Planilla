package com.todocodeacademy.sistema_planilla.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Empresa {

    Long idEmpresa;

    String razonSocial;
    String ruc;

    String direccion;
    String telefono;
    String correo;

    Instant createdAt;
    Instant updatedAt;

    // =========================
    // 🏗️ CONSTRUCTOR
    // =========================
    public Empresa(String razonSocial, String ruc) {
        this.razonSocial = razonSocial;
        this.ruc = ruc;
    }

    // =========================
    // 🔄 RECONSTRUCCIÓN
    // =========================
    public static Empresa reconstruir(
            Long id,
            String razonSocial,
            String ruc,
            String direccion,
            String telefono,
            String correo,
            Instant createdAt,
            Instant updatedAt
    ) {
        Empresa e = new Empresa(razonSocial, ruc);
        e.idEmpresa = id;
        e.direccion = direccion;
        e.telefono = telefono;
        e.correo = correo;
        e.createdAt = createdAt;
        e.updatedAt = updatedAt;
        return e;
    }

    // =========================
    // 🧠 LÓGICA
    // =========================
    public void actualizarDatos(String direccion, String telefono, String correo) {
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
    }
}