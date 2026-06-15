package com.todocodeacademy.sistema_planilla.domain.model;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoPlanilla;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Planilla {

    Long idPlanilla;

    Integer mes;
    Integer anio;

    TipoPlanilla tipoPlanilla;

    LocalDate fechaGenerada;

    ParametroLegal parametroLegal;

    List<DetallePlanilla> detallesPlanilla;

    boolean cerrada;

    Instant createdAt;
    Instant updatedAt;

    // =========================
    // 🏗️ CONSTRUCTOR
    // =========================
    public Planilla(
            Integer mes,
            Integer anio,
            TipoPlanilla tipoPlanilla,
            ParametroLegal parametroLegal
    ) {

        if (mes == null || mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mes inválido");
        }

        if (anio == null || anio <= 0) {
            throw new IllegalArgumentException("Año inválido");
        }

        if (tipoPlanilla == null) {
            throw new IllegalArgumentException("Tipo de planilla obligatorio");
        }

        this.mes = mes;
        this.anio = anio;
        this.tipoPlanilla = tipoPlanilla;

        this.parametroLegal = parametroLegal;

        this.fechaGenerada = LocalDate.now();

        this.detallesPlanilla = new ArrayList<>();

        this.cerrada = false;
    }

    // =========================
    // 🔄 RECONSTRUCCIÓN
    // =========================
    public static Planilla reconstruir(
            Long idPlanilla,
            Integer mes,
            Integer anio,
            TipoPlanilla tipoPlanilla,
            LocalDate fechaGenerada,
            ParametroLegal parametroLegal,
            List<DetallePlanilla> detallesPlanilla,
            boolean cerrada,
            Instant createdAt,
            Instant updatedAt
    ) {

        Planilla p = new Planilla(
                mes,
                anio,
                tipoPlanilla,
                parametroLegal
        );

        p.idPlanilla = idPlanilla;

        p.fechaGenerada = fechaGenerada;

        p.detallesPlanilla =
                detallesPlanilla != null
                        ? new ArrayList<>(detallesPlanilla)
                        : new ArrayList<>();

        p.cerrada = cerrada;

        p.createdAt = createdAt;
        p.updatedAt = updatedAt;

        return p;
    }

    // =========================
    // 🧠 LÓGICA DE NEGOCIO
    // =========================

    public void agregarDetalle(DetallePlanilla detalle) {

        validarPlanillaAbierta();

        if (detalle == null) {
            throw new IllegalArgumentException("Detalle inválido");
        }

        this.detallesPlanilla.add(detalle);
    }

    public void eliminarDetalle(DetallePlanilla detalle) {

        validarPlanillaAbierta();

        this.detallesPlanilla.remove(detalle);
    }

    public void cerrarPlanilla() {

        if (this.detallesPlanilla.isEmpty()) {
            throw new IllegalStateException(
                    "No se puede cerrar una planilla vacía"
            );
        }

        this.cerrada = true;
    }

    public void abrirPlanilla() {
        this.cerrada = false;
    }

    public boolean estaCerrada() {
        return cerrada;
    }

    // =========================
    // 💰 TOTALES GENERALES
    // =========================

    public BigDecimal calcularTotalBruto() {

        return detallesPlanilla.stream()
                .map(DetallePlanilla::getSueldoBruto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalNeto() {

        return detallesPlanilla.stream()
                .map(DetallePlanilla::getSueldoNeto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalDescuentos() {

        return detallesPlanilla.stream()
                .map(DetallePlanilla::getTotalDescuento)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalIngresosAdicionales() {

        return detallesPlanilla.stream()
                .map(DetallePlanilla::getTotalIngresosAdicionales)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalAportesEmpleador() {

        return detallesPlanilla.stream()
                .map(DetallePlanilla::getTotalAportesEmpleador)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // =========================
    // 📊 MÉTODOS ÚTILES
    // =========================

    public int cantidadDetalles() {
        return detallesPlanilla.size();
    }

    public List<DetallePlanilla> obtenerDetalles() {
        return Collections.unmodifiableList(detallesPlanilla);
    }

    // =========================
    // 🔒 VALIDACIONES
    // =========================

    private void validarPlanillaAbierta() {

        if (this.cerrada) {
            throw new IllegalStateException(
                    "La planilla está cerrada"
            );
        }
    }
}