package com.todocodeacademy.sistema_planilla.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistorialPuesto {

    Long idHistorial;

    Empleado empleado;
    Puesto puesto;

    LocalDate fechaInicio;
    LocalDate fechaFin;

    Instant createdAt;
    Instant updatedAt;

    // =========================
    // 🏗️ CONSTRUCTOR
    // =========================
    public HistorialPuesto(Empleado empleado, Puesto puesto, LocalDate fechaInicio) {
        this.empleado = empleado;
        this.puesto = puesto;
        this.fechaInicio = fechaInicio;
    }

    // =========================
    // 🔄 RECONSTRUCCIÓN
    // =========================
    public static HistorialPuesto reconstruir(
            Long id,
            Empleado empleado,
            Puesto puesto,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            Instant createdAt,
            Instant updatedAt
    ) {
        HistorialPuesto h = new HistorialPuesto(empleado, puesto, fechaInicio);

        h.idHistorial = id;
        h.fechaFin = fechaFin;
        h.createdAt = createdAt;
        h.updatedAt = updatedAt;

        return h;
    }

    // =========================
    // 🧠 LÓGICA
    // =========================
    public void finalizar(LocalDate fechaFin) {
        if (fechaFin == null || fechaFin.isBefore(this.fechaInicio)) {
            throw new IllegalArgumentException("Fecha fin inválida");
        }
        this.fechaFin = fechaFin;
    }

    public boolean estaActivo() {
        return this.fechaFin == null;
    }
}