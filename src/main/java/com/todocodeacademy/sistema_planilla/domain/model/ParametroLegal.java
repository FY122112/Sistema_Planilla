package com.todocodeacademy.sistema_planilla.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParametroLegal {

    Long idParametro;

    String codigo;

    String descripcion;

    BigDecimal valor;

    LocalDate fechaInicioVigencia;

    LocalDate fechaFinVigencia;

    Instant createdAt;

    Instant updatedAt;

    // =========================
    // 🏗️ CONSTRUCTOR
    // =========================

    public ParametroLegal(
            String codigo,
            String descripcion,
            BigDecimal valor,
            LocalDate fechaInicioVigencia,
            LocalDate fechaFinVigencia
    ) {

       // validarCodigo(codigo);
        //validarValor(valor);

        this.codigo = codigo.trim().toUpperCase();
        this.descripcion = descripcion;
        this.valor = valor;
        this.fechaInicioVigencia = fechaInicioVigencia;
        this.fechaFinVigencia = fechaFinVigencia;
    }

    // =========================
    // 🔄 RECONSTRUCCIÓN
    // =========================

    public static ParametroLegal reconstruir(
            Long idParametro,
            String codigo,
            String descripcion,
            BigDecimal valor,
            LocalDate fechaInicioVigencia,
            LocalDate fechaFinVigencia,
            Instant createdAt,
            Instant updatedAt
    ) {

        ParametroLegal parametro = new ParametroLegal(
                codigo,
                descripcion,
                valor,
                fechaInicioVigencia,
                fechaFinVigencia
        );

        parametro.idParametro = idParametro;
        parametro.createdAt = createdAt;
        parametro.updatedAt = updatedAt;

        return parametro;
    }

    // =========================
    // 🧠 LÓGICA DE NEGOCIO
    // =========================

    public void actualizarCodigo(String codigo) {

        //validarCodigo(codigo);

        this.codigo = codigo.trim().toUpperCase();
    }

    public void actualizarDescripcion(String descripcion) {

        this.descripcion = descripcion;
    }

    public void actualizarValor(BigDecimal valor) {

        //validarValor(valor);

        this.valor = valor;
    }

    public void actualizarFechaInicioVigencia(
            LocalDate fechaInicioVigencia
    ) {

        if (fechaInicioVigencia == null) {

            throw new IllegalArgumentException(
                    "La fecha de inicio es obligatoria");
        }

        this.fechaInicioVigencia = fechaInicioVigencia;
    }

    public void actualizarFechaFinVigencia(
            LocalDate fechaFinVigencia
    ) {

        if (
                fechaFinVigencia != null &&
                        fechaInicioVigencia != null &&
                        fechaFinVigencia.isBefore(fechaInicioVigencia)
        ) {

            throw new IllegalArgumentException(
                    "La fecha fin no puede ser menor a la fecha inicio");
        }

        this.fechaFinVigencia = fechaFinVigencia;
    }

    // =========================
    // ✅ VALIDACIONES
    // =========================

/*    private void validarCodigo(String codigo) {

        if (codigo == null || codigo.isBlank()) {

            throw new IllegalArgumentException(
                    "El código es obligatorio");
        }
    }

    private void validarValor(BigDecimal valor) {

        if (
                valor == null ||
                        valor.compareTo(BigDecimal.ZERO) < 0
        ) {

            throw new IllegalArgumentException(
                    "El valor es inválido");
        }
    }*/
}