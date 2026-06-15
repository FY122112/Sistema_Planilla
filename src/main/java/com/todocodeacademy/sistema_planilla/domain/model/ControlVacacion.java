package com.todocodeacademy.sistema_planilla.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ControlVacacion {

    Long idControlVacacion;

    Empleado empleado;

    LocalDate fechaInicioPeriodo;

    LocalDate fechaFinPeriodo;

    Integer diasGanados;

    Integer diasGozados;

    Instant createdAt;

    Instant updatedAt;

    // =========================
    // 🏗️ CONSTRUCTOR
    // =========================

    public ControlVacacion(
            Empleado empleado,
            LocalDate fechaInicioPeriodo,
            LocalDate fechaFinPeriodo,
            Integer diasGanados
    ) {

        validarPeriodo(
                fechaInicioPeriodo,
                fechaFinPeriodo
        );

        this.empleado = empleado;

        this.fechaInicioPeriodo = fechaInicioPeriodo;

        this.fechaFinPeriodo = fechaFinPeriodo;

        this.diasGanados =
                diasGanados != null ? diasGanados : 30;

        this.diasGozados = 0;
    }

    // =========================
    // 🔄 RECONSTRUCCIÓN
    // =========================



    public static ControlVacacion reconstruir(

            Long id,

            Empleado empleado,

            LocalDate fechaInicioPeriodo,

            LocalDate fechaFinPeriodo,

            Integer diasGanados,

            Integer diasGozados,

            Instant createdAt,

            Instant updatedAt

    ) {

        ControlVacacion control =
                new ControlVacacion(
                        empleado,
                        fechaInicioPeriodo,
                        fechaFinPeriodo,
                        diasGanados
                );

        control.idControlVacacion = id;

        control.diasGozados =
                diasGozados != null ? diasGozados : 0;

        control.createdAt = createdAt;

        control.updatedAt = updatedAt;

        return control;
    }

    // =========================
    // 🧠 LÓGICA NEGOCIO
    // =========================



    public void asignarDias(Integer dias) {

        validarDias(dias);

        this.diasGanados += dias;
    }

    public void gozarDias(Integer dias) {

        validarDias(dias);

        if (dias > getDiasPendientes()) {

            throw new IllegalStateException(
                    "No hay suficientes días disponibles");
        }

        this.diasGozados += dias;
    }

    public void revertirDias(Integer dias) {

        validarDias(dias);

        this.diasGozados =
                Math.max(
                        this.diasGozados - dias,
                        0
                );
    }

    // =========================
    // 📊 CÁLCULO
    // =========================

    public Integer getDiasPendientes() {

        return diasGanados - diasGozados;
    }

    // =========================
    // ✏️ ACTUALIZACIONES
    // =========================

    public void actualizarPeriodo(
            LocalDate inicio,
            LocalDate fin
    ) {

        validarPeriodo(inicio, fin);

        this.fechaInicioPeriodo = inicio;
        this.fechaFinPeriodo = fin;
    }

    // =========================
    // ✅ VALIDACIONES
    // =========================

    private static void validarDias(Integer dias) {

        if (dias == null || dias <= 0) {

            throw new IllegalArgumentException(
                    "Los días son inválidos");
        }
    }

    private static void validarPeriodo(
            LocalDate inicio,
            LocalDate fin
    ) {

        if (inicio == null || fin == null) {

            throw new IllegalArgumentException(
                    "Las fechas son obligatorias");
        }

        if (fin.isBefore(inicio)) {

            throw new IllegalArgumentException(
                    "La fecha fin no puede ser menor a la fecha inicio");
        }
    }

}