package com.todocodeacademy.sistema_planilla.domain.model;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.MetodoCalculado;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoSistemaPensiones;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConceptoPago {

    Long idConcepto;

    String codigoSunat;
    String nombreConcepto;
    String tipoConcepto;
    String metodoCalculo;

    boolean esRemunerativo;

    BigDecimal valorReferencial;

    String tipoSistemaPensiones;

    boolean afectoEssalud;

    String descripcion;

    Instant createdAt;
    Instant updatedAt;

    // =========================
    // 🏗️ CONSTRUCTOR
    // =========================
    public ConceptoPago(
            String codigoSunat,
            String nombreConcepto,
            String tipoConcepto,
            String metodoCalculo,
            boolean esRemunerativo
    ) {
        this.codigoSunat = codigoSunat;
        this.nombreConcepto = nombreConcepto;
        this.tipoConcepto = tipoConcepto;
        this.metodoCalculo = metodoCalculo;
        this.esRemunerativo = esRemunerativo;

        this.valorReferencial = BigDecimal.ZERO;
        this.afectoEssalud = true;
    }

    // =========================
    // 🔄 RECONSTRUCCIÓN (BD)
    // =========================
    public static ConceptoPago reconstruir(
            Long id,
            String codigoSunat,
            String nombreConcepto,
            String tipoConcepto,
            String metodoCalculo,
            boolean esRemunerativo,
            BigDecimal valorReferencial,
            String tipoSistemaPensiones,
            boolean afectoEssalud,
            String descripcion,
            Instant createdAt,
            Instant updatedAt
    ) {
        ConceptoPago c = new ConceptoPago(
                codigoSunat,
                nombreConcepto,
                tipoConcepto,
                metodoCalculo,
                esRemunerativo
        );

        c.idConcepto = id;
        c.valorReferencial = valorReferencial != null ? valorReferencial : BigDecimal.ZERO;
        c.tipoSistemaPensiones = tipoSistemaPensiones;
        c.afectoEssalud = afectoEssalud;
        c.descripcion = descripcion;
        c.createdAt = createdAt;
        c.updatedAt = updatedAt;

        return c;
    }

    // =========================
    // 🧠 LÓGICA DE NEGOCIO
    // =========================

    public void actualizarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre inválido");
        }
        this.nombreConcepto = nombre;
    }

    public void actualizarValorReferencial(BigDecimal valor) {
        this.valorReferencial = valor != null ? valor : BigDecimal.ZERO;
    }

    public void marcarComoNoRemunerativo() {
        this.esRemunerativo = false;
    }

    public void marcarComoRemunerativo() {
        this.esRemunerativo = true;
    }

    // =========================
    // 📌 GETTERS SEGUROS
    // =========================

    public BigDecimal getValorReferencial() {
        return valorReferencial != null ? valorReferencial : BigDecimal.ZERO;
    }

    public boolean isAfectoEssalud() {
        return afectoEssalud;
    }

    public boolean isEsRemunerativo() {
        return esRemunerativo;
    }

    public void actualizarDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void actualizarTipoSistemaPensiones(String tipo) {
        this.tipoSistemaPensiones = tipo;
    }

    public void actualizarAfectoEssalud(boolean afecto) {
        this.afectoEssalud = afecto;
    }


}