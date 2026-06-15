package com.todocodeacademy.sistema_planilla.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovimientoPlanilla {

    Long idMovimiento;

    DetallePlanilla detallePlanilla;
    ConceptoPago concepto;

    BigDecimal monto;

    Instant createdAt;
    Instant updatedAt;

    // =========================
    // 🏗️ CONSTRUCTOR
    // =========================
    public MovimientoPlanilla(DetallePlanilla detallePlanilla, ConceptoPago concepto, BigDecimal monto) {

        if (detallePlanilla == null) {
            throw new IllegalArgumentException("DetallePlanilla es obligatorio");
        }

        if (concepto == null) {
            throw new IllegalArgumentException("ConceptoPago es obligatorio");
        }

        this.detallePlanilla = detallePlanilla;
        this.concepto = concepto;
        this.monto = monto != null ? monto : BigDecimal.ZERO;
    }

    // =========================
    // 🔄 RECONSTRUCCIÓN
    // =========================
    public static MovimientoPlanilla reconstruir(
            Long id,
            DetallePlanilla detallePlanilla,
            ConceptoPago concepto,
            BigDecimal monto,
            Instant createdAt,
            Instant updatedAt
    ) {
        MovimientoPlanilla m = new MovimientoPlanilla(detallePlanilla, concepto, monto);

        m.idMovimiento = id;
        m.createdAt = createdAt;
        m.updatedAt = updatedAt;

        return m;
    }

    // =========================
    // 🧠 LÓGICA
    // =========================
    public boolean esIngreso() {
        return concepto != null && concepto.isEsRemunerativo();
    }

    public boolean esDescuento() {
        return !esIngreso();
    }
}