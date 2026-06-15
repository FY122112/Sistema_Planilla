package com.todocodeacademy.sistema_planilla.domain.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Banco {

    Long idBanco;
    String nombreBanco;
    String codigoBanco;

    Instant createdAt;
    Instant updatedAt;

    private Banco() {}

    // 🏗️ CREAR
    public static Banco crear(String nombreBanco, String codigoBanco) {
        if (nombreBanco == null || nombreBanco.isBlank()) {
            throw new IllegalArgumentException("Nombre banco requerido");
        }
        if (codigoBanco == null || codigoBanco.isBlank()) {
            throw new IllegalArgumentException("Código banco requerido");
        }

        Banco b = new Banco();
        b.nombreBanco = nombreBanco;
        b.codigoBanco = codigoBanco;
        return b;
    }

    // 🔄 RECONSTRUIR
    public static Banco reconstruir(
            Long id,
            String nombre,
            String codigo,
            Instant createdAt,
            Instant updatedAt
    ) {
        Banco b = new Banco();
        b.idBanco = id;
        b.nombreBanco = nombre;
        b.codigoBanco = codigo;
        b.createdAt = createdAt;
        b.updatedAt = updatedAt;
        return b;
    }

    // 🧠 LÓGICA
    public void actualizarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre inválido");
        }
        this.nombreBanco = nombre;
    }
}