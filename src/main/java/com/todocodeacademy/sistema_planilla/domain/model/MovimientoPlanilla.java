package com.todocodeacademy.sistema_planilla.domain.model;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;
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

    private MovimientoPlanilla() {
    }

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
    // A diferencia del constructor de creación, detallePlanilla puede llegar null: el
    // mapper de entidades reconstruye los movimientos antes que su DetallePlanilla padre
    // (para evitar el ciclo detalle <-> movimiento) y los vincula después con
    // vincularDetalle(...), igual que DetallePlanilla.vincularPlanilla(...).
    public static MovimientoPlanilla reconstruir(
            Long id,
            DetallePlanilla detallePlanilla,
            ConceptoPago concepto,
            BigDecimal monto,
            Instant createdAt,
            Instant updatedAt
    ) {
        if (concepto == null) {
            throw new IllegalArgumentException("ConceptoPago es obligatorio");
        }

        MovimientoPlanilla m = new MovimientoPlanilla();

        m.idMovimiento = id;
        m.detallePlanilla = detallePlanilla;
        m.concepto = concepto;
        m.monto = monto != null ? monto : BigDecimal.ZERO;
        m.createdAt = createdAt;
        m.updatedAt = updatedAt;

        return m;
    }

    public void vincularDetalle(DetallePlanilla detallePlanilla) {

        if (detallePlanilla == null) {
            throw new IllegalArgumentException("DetallePlanilla es obligatorio");
        }

        this.detallePlanilla = detallePlanilla;
    }

    // =========================
    // 🧠 LÓGICA
    // =========================
    public boolean esIngreso() {
        return concepto != null && concepto.getTipoConcepto() == TipoConcepto.INGRESO;
    }

    public boolean esDescuento() {
        return concepto != null && concepto.getTipoConcepto() == TipoConcepto.DESCUENTO;
    }

    public boolean esAporteEmpleador() {
        return concepto != null && concepto.getTipoConcepto() == TipoConcepto.APORTE_EMPLEADOR;
    }
}