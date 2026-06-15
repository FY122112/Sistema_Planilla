package com.todocodeacademy.sistema_planilla.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SistemaPension {

    Long idSistema;
    String nombre; // AFP / ONP
    String tipo;   // PUBLICO / PRIVADO

    BigDecimal porcentajeAporte;   // % que aporta el empleado
    BigDecimal porcentajeComision; // comisión AFP

    Instant createdAt;
    Instant updatedAt;

    // =========================
    // 🏗️ CONSTRUCTOR
    // =========================
    public SistemaPension(String nombre, String tipo,
                          BigDecimal porcentajeAporte,
                          BigDecimal porcentajeComision) {

        this.nombre = nombre;
        this.tipo = tipo;
        this.porcentajeAporte = porcentajeAporte != null ? porcentajeAporte : BigDecimal.ZERO;
        this.porcentajeComision = porcentajeComision != null ? porcentajeComision : BigDecimal.ZERO;
    }

    // =========================
    // 🔄 RECONSTRUCCIÓN
    // =========================
    public static SistemaPension reconstruir(
            Long id,
            String nombre,
            String tipo,
            BigDecimal aporte,
            BigDecimal comision,
            Instant createdAt,
            Instant updatedAt
    ) {
        SistemaPension s = new SistemaPension(nombre, tipo, aporte, comision);
        s.idSistema = id;
        s.createdAt = createdAt;
        s.updatedAt = updatedAt;
        return s;
    }

    public void actualizarNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        this.nombre = nombre;
        this.updatedAt = Instant.now();
    }

    public void actualizarTipo(String tipo) {

        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo es obligatorio");
        }

        this.tipo = tipo;
        this.updatedAt = Instant.now();
    }

    public void actualizarPorcentajeAporte(BigDecimal porcentajeAporte) {

        if (porcentajeAporte == null ||
                porcentajeAporte.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "El porcentaje de aporte es inválido"
            );
        }

        this.porcentajeAporte = porcentajeAporte;
        this.updatedAt = Instant.now();
    }

    public void actualizarPorcentajeComision(BigDecimal porcentajeComision) {

        if (porcentajeComision == null ||
                porcentajeComision.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "El porcentaje de comisión es inválido"
            );
        }

        this.porcentajeComision = porcentajeComision;
        this.updatedAt = Instant.now();
    }


    // =========================
    // 🧠 LÓGICA
    // =========================
    public BigDecimal calcularDescuento(BigDecimal sueldo) {
        if (sueldo == null) return BigDecimal.ZERO;

        BigDecimal aporte = sueldo.multiply(porcentajeAporte);
        BigDecimal comision = sueldo.multiply(porcentajeComision);

        return aporte.add(comision);
    }
}