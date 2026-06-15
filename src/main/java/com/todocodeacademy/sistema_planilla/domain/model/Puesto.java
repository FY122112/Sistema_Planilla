package com.todocodeacademy.sistema_planilla.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Puesto {

    Long idPuesto;

    String nombre;

    BigDecimal salarioBase;

    Integer jornadaLaboralHoras;

    LocalTime horaInicioJornada;

    LocalTime horaFinJornada;

    String descripcion;

    Instant createdAt;

    Instant updatedAt;

    // =========================
    // 🏗️ CONSTRUCTOR
    // =========================

    public Puesto(
            String nombre,
            BigDecimal salarioBase,
            Integer jornadaLaboralHoras,
            LocalTime horaInicioJornada,
            LocalTime horaFinJornada,
            String descripcion
    ) {

        validarNombre(nombre);
        validarSalario(salarioBase);

        this.nombre = nombre;
        this.salarioBase = salarioBase;
        this.jornadaLaboralHoras = jornadaLaboralHoras;
        this.horaInicioJornada = horaInicioJornada;
        this.horaFinJornada = horaFinJornada;
        this.descripcion = descripcion;
    }

    // =========================
    // 🔄 RECONSTRUCCIÓN
    // =========================

    public static Puesto reconstruir(
            Long idPuesto,
            String nombre,
            BigDecimal salarioBase,
            Integer jornadaLaboralHoras,
            LocalTime horaInicioJornada,
            LocalTime horaFinJornada,
            String descripcion,
            Instant createdAt,
            Instant updatedAt
    ) {

        Puesto puesto = new Puesto(
                nombre,
                salarioBase,
                jornadaLaboralHoras,
                horaInicioJornada,
                horaFinJornada,
                descripcion
        );

        puesto.idPuesto = idPuesto;
        puesto.createdAt = createdAt;
        puesto.updatedAt = updatedAt;

        return puesto;
    }

    // =========================
    // 🧠 LÓGICA DE NEGOCIO
    // =========================

    public void actualizarNombre(String nombre) {

        validarNombre(nombre);

        this.nombre = nombre;
    }

    public void actualizarDescripcion(String descripcion) {

        this.descripcion = descripcion;
    }

    public void actualizarSalarioBase(BigDecimal salarioBase) {

        validarSalario(salarioBase);

        this.salarioBase = salarioBase;
    }

    public void actualizarJornadaLaboralHoras(Integer horas) {

        if (horas != null && horas <= 0) {

            throw new IllegalArgumentException(
                    "La jornada laboral debe ser mayor a cero");
        }

        this.jornadaLaboralHoras = horas;
    }

    public void actualizarHoraInicioJornada(LocalTime horaInicioJornada) {

        this.horaInicioJornada = horaInicioJornada;
    }

    public void actualizarHoraFinJornada(LocalTime horaFinJornada) {

        this.horaFinJornada = horaFinJornada;
    }

    // =========================
    // ✅ VALIDACIONES
    // =========================

    private void validarNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre del puesto es obligatorio");
        }
    }

    private void validarSalario(BigDecimal salarioBase) {

        if (salarioBase == null ||
                salarioBase.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "El salario base es inválido");
        }
    }
}