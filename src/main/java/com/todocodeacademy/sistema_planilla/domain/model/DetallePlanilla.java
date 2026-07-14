package com.todocodeacademy.sistema_planilla.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetallePlanilla {

    Long idDetalle;

    Planilla planilla;
    Empleado empleado;

    BigDecimal sueldoBase;
    BigDecimal asignacionFamiliar;

    BigDecimal remuneracionComputableAfecta;

    BigDecimal totalIngresosAdicionales;
    BigDecimal totalDescuento;
    BigDecimal totalAportesEmpleador;

    BigDecimal sueldoBruto;
    BigDecimal sueldoNeto;

    List<MovimientoPlanilla> movimientos;

    Boleta boleta;

    Instant createdAt;
    Instant updatedAt;

    // =========================
    // 🏗️ CONSTRUCTOR
    // =========================
    private DetallePlanilla() {
    }

    public DetallePlanilla(
            Planilla planilla,
            Empleado empleado,
            BigDecimal sueldoBase
    ) {

        if (planilla == null) {
            throw new IllegalArgumentException("La planilla es obligatoria");
        }

        if (empleado == null) {
            throw new IllegalArgumentException("El empleado es obligatorio");
        }

        this.planilla = planilla;
        this.empleado = empleado;

        this.sueldoBase =
                sueldoBase != null ? sueldoBase : BigDecimal.ZERO;

        this.asignacionFamiliar = BigDecimal.ZERO;
        this.remuneracionComputableAfecta = BigDecimal.ZERO;

        this.totalIngresosAdicionales = BigDecimal.ZERO;
        this.totalDescuento = BigDecimal.ZERO;
        this.totalAportesEmpleador = BigDecimal.ZERO;

        this.sueldoBruto = BigDecimal.ZERO;
        this.sueldoNeto = BigDecimal.ZERO;

        this.movimientos = new ArrayList<>();
    }

    // =========================
    // 🔄 RECONSTRUCCIÓN
    // =========================
    // A diferencia del constructor de creación, aquí `planilla` y `empleado` pueden
    // llegar null: el mapper de entidades reconstruye el detalle antes que su planilla
    // padre (para evitar el ciclo planilla <-> detalle) y lo vincula después con
    // vincularPlanilla(...); BoletaEntMapper también arma una referencia liviana con
    // solo el id del detalle, sin cargar el empleado, para no traer el agregado completo.
    public static DetallePlanilla reconstruir(
            Long id,
            Planilla planilla,
            Empleado empleado,
            BigDecimal sueldoBase,
            BigDecimal asignacionFamiliar,
            BigDecimal remuneracionComputableAfecta,
            BigDecimal totalIngresosAdicionales,
            BigDecimal totalDescuento,
            BigDecimal totalAportesEmpleador,
            BigDecimal sueldoBruto,
            BigDecimal sueldoNeto,
            List<MovimientoPlanilla> movimientos,
            Boleta boleta,
            Instant createdAt,
            Instant updatedAt
    ) {

        DetallePlanilla d = new DetallePlanilla();

        d.idDetalle = id;
        d.planilla = planilla;
        d.empleado = empleado;

        d.sueldoBase = safe(sueldoBase);
        d.asignacionFamiliar = safe(asignacionFamiliar);
        d.remuneracionComputableAfecta =
                safe(remuneracionComputableAfecta);

        d.totalIngresosAdicionales =
                safe(totalIngresosAdicionales);

        d.totalDescuento =
                safe(totalDescuento);

        d.totalAportesEmpleador =
                safe(totalAportesEmpleador);

        d.sueldoBruto =
                safe(sueldoBruto);

        d.sueldoNeto =
                safe(sueldoNeto);

        d.movimientos =
                movimientos != null
                        ? new ArrayList<>(movimientos)
                        : new ArrayList<>();

        d.boleta = boleta;

        d.createdAt = createdAt;
        d.updatedAt = updatedAt;

        return d;
    }

    // Vincula el detalle con su planilla padre una vez que esta ya fue reconstruida.
    public void vincularPlanilla(Planilla planilla) {

        if (planilla == null) {
            throw new IllegalArgumentException("La planilla es obligatoria");
        }

        this.planilla = planilla;
    }

    // =========================
    // 🧠 LÓGICA DE NEGOCIO
    // =========================

    public void agregarMovimiento(MovimientoPlanilla movimiento) {

        if (movimiento == null) {
            throw new IllegalArgumentException("Movimiento inválido");
        }

        this.movimientos.add(movimiento);

        recalcularTotales();
    }

    public void eliminarMovimiento(MovimientoPlanilla movimiento) {

        if (movimiento == null) return;

        this.movimientos.remove(movimiento);

        recalcularTotales();
    }

    public void actualizarAsignacionFamiliar(BigDecimal monto) {

        this.asignacionFamiliar = safe(monto);

        recalcularTotales();
    }

    public void actualizarRemuneracionComputable(BigDecimal monto) {

        this.remuneracionComputableAfecta = safe(monto);
    }

    public void recalcularTotales() {

        totalIngresosAdicionales = BigDecimal.ZERO;
        totalDescuento = BigDecimal.ZERO;
        totalAportesEmpleador = BigDecimal.ZERO;

        for (MovimientoPlanilla mov : movimientos) {

            if (mov.esIngreso()) {

                totalIngresosAdicionales =
                        totalIngresosAdicionales.add(
                                safe(mov.getMonto())
                        );

            } else {

                totalDescuento =
                        totalDescuento.add(
                                safe(mov.getMonto())
                        );
            }
        }

        sueldoBruto =
                sueldoBase
                        .add(asignacionFamiliar)
                        .add(totalIngresosAdicionales);

        sueldoNeto =
                sueldoBruto.subtract(totalDescuento);
    }

    // =========================
    // 📊 MÉTODOS ÚTILES
    // =========================

    public List<MovimientoPlanilla> obtenerMovimientos() {
        return Collections.unmodifiableList(movimientos);
    }

    public int cantidadMovimientos() {
        return movimientos.size();
    }

    public boolean tieneDescuentos() {
        return totalDescuento.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean tieneIngresosAdicionales() {
        return totalIngresosAdicionales.compareTo(BigDecimal.ZERO) > 0;
    }

    // =========================
    // 🔒 HELPERS
    // =========================

    private static BigDecimal safe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}