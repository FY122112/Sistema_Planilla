package com.todocodeacademy.sistema_planilla.domain.model;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoBoleta;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Boleta {

    Long idBoleta;

    DetallePlanilla detallePlanilla;

    LocalDate fechaEmision;

    Integer periodoMes;
    Integer periodoAnio;

    BigDecimal sueldoBruto;
    BigDecimal totalDescuento;
    BigDecimal sueldoNeto;

    String rutaPdf;

    EstadoBoleta estadoBoleta;

    Instant fechaFirma;

    Instant createdAt;
    Instant updatedAt;

    private Boleta() {}

    // =========================
    // 🏗️ CREACIÓN
    // =========================
    public static Boleta crear(
            DetallePlanilla detallePlanilla,
            LocalDate fechaEmision,
            Integer periodoMes,
            Integer periodoAnio,
            BigDecimal sueldoBruto,
            BigDecimal totalDescuento
    ) {
        if (detallePlanilla == null) {
            throw new IllegalArgumentException("Detalle requerido");
        }
        if (fechaEmision == null) {
            throw new IllegalArgumentException("Fecha emisión requerida");
        }

        Boleta b = new Boleta();

        b.detallePlanilla = detallePlanilla;
        b.fechaEmision = fechaEmision;
        b.periodoMes = periodoMes;
        b.periodoAnio = periodoAnio;

        b.sueldoBruto = safe(sueldoBruto);
        b.totalDescuento = safe(totalDescuento);

        b.estadoBoleta = EstadoBoleta.GENERADA;

        b.calcularSueldoNeto();

        return b;
    }

    // =========================
    // 🔄 RECONSTRUCCIÓN (BD)
    // =========================
    public static Boleta reconstruir(
            Long id,
            DetallePlanilla detalle,
            LocalDate fechaEmision,
            Integer periodoMes,
            Integer periodoAnio,
            BigDecimal sueldoBruto,
            BigDecimal totalDescuento,
            BigDecimal sueldoNeto,
            String rutaPdf,
            EstadoBoleta estado,
            Instant fechaFirma,
            Instant createdAt,
            Instant updatedAt
    ) {
        // A diferencia de crear(...), aquí no se sustituyen los null por defaults: esta
        // fábrica también se usa para construir payloads parciales de actualización
        // (BoletaService.update solo aplica los campos que vengan no-null), y forzar
        // sueldoBruto/totalDescuento a ZERO o el estado a GENERADA reventaría esa lógica.
        Boleta b = new Boleta();

        b.idBoleta = id;
        b.detallePlanilla = detalle;
        b.fechaEmision = fechaEmision;
        b.periodoMes = periodoMes;
        b.periodoAnio = periodoAnio;

        b.sueldoBruto = sueldoBruto;
        b.totalDescuento = totalDescuento;
        b.sueldoNeto = sueldoNeto;

        b.rutaPdf = rutaPdf;
        b.estadoBoleta = estado;
        b.fechaFirma = fechaFirma;

        b.createdAt = createdAt;
        b.updatedAt = updatedAt;

        return b;
    }

    private static BigDecimal safe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    // =========================
    // 🧠 LÓGICA DE NEGOCIO
    // =========================

    public void calcularSueldoNeto() {
        this.sueldoNeto = safe(sueldoBruto).subtract(safe(totalDescuento));
    }

    public void asignarRutaPdf(String ruta) {
        if (ruta == null || ruta.isBlank()) {
            throw new IllegalArgumentException("Ruta inválida");
        }
        this.rutaPdf = ruta;
    }

    public void marcarComoGenerada() {
        this.estadoBoleta = EstadoBoleta.GENERADA;
    }

    public void marcarComoFirmada() {
        this.estadoBoleta = EstadoBoleta.FIRMADA;
        this.fechaFirma = Instant.now();
    }

    public void marcarComoEnviada() {
        this.estadoBoleta = EstadoBoleta.ENVIADA;
    }

    public void marcarComoPagada() {
        this.estadoBoleta = EstadoBoleta.PAGADA;
    }

    public boolean estaPagada() {
        return EstadoBoleta.PAGADA.equals(this.estadoBoleta);
    }

    public boolean estaGenerada() {
        return EstadoBoleta.GENERADA.equals(this.estadoBoleta);
    }

    public void actualizarMontos(BigDecimal bruto, BigDecimal descuento) {
        this.sueldoBruto = bruto != null ? bruto : this.sueldoBruto;
        this.totalDescuento = descuento != null ? descuento : this.totalDescuento;
        calcularSueldoNeto();
    }

}