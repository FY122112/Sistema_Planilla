package com.todocodeacademy.sistema_planilla.domain.model;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.MetodoCalculado;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoSistemaPensiones;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConceptoPago {

    Long idConcepto;

    String codigoSunat;
    String nombreConcepto;

    TipoConcepto tipoConcepto;
    MetodoCalculado metodoCalculado;

    boolean esRemunerativo;

    BigDecimal valorReferencial;

    TipoSistemaPensiones tipoSistemaPensiones;

    boolean afectoEssalud;

    String descripcion;

    Instant createdAt;
    Instant updatedAt;

    public ConceptoPago(
            String codigoSunat,
            String nombreConcepto,
            TipoConcepto tipoConcepto,
            MetodoCalculado metodoCalculado,
            boolean esRemunerativo
    ) {

        validarNombre(nombreConcepto);

        this.codigoSunat = codigoSunat;
        this.nombreConcepto = nombreConcepto;
        this.tipoConcepto = tipoConcepto;
        this.metodoCalculado = metodoCalculado;
        this.esRemunerativo = esRemunerativo;
        this.valorReferencial = BigDecimal.ZERO;
        this.afectoEssalud = true;
    }

    public static ConceptoPago reconstruir(
            Long idConcepto,
            String codigoSunat,
            String nombreConcepto,
            TipoConcepto tipoConcepto,
            MetodoCalculado metodoCalculado,
            boolean esRemunerativo,
            BigDecimal valorReferencial,
            TipoSistemaPensiones tipoSistemaPensiones,
            boolean afectoEssalud,
            String descripcion,
            Instant createdAt,
            Instant updatedAt
    ) {

        ConceptoPago concepto = new ConceptoPago(
                codigoSunat,
                nombreConcepto,
                tipoConcepto,
                metodoCalculado,
                esRemunerativo
        );

        concepto.idConcepto = idConcepto;
        concepto.valorReferencial = valorReferencial;
        concepto.tipoSistemaPensiones = tipoSistemaPensiones;
        concepto.afectoEssalud = afectoEssalud;
        concepto.descripcion = descripcion;
        concepto.createdAt = createdAt;
        concepto.updatedAt = updatedAt;

        return concepto;
    }

    public void actualizarNombre(String nombre) {
        validarNombre(nombre);
        this.nombreConcepto = nombre;
    }

    public void actualizarValorReferencial(BigDecimal valor) {
        this.valorReferencial = valor;
    }

    public void actualizarDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void actualizarTipoSistemaPensiones(TipoSistemaPensiones tipo) {
        this.tipoSistemaPensiones = tipo;
    }

    public void actualizarAfectoEssalud(boolean afecto) {
        this.afectoEssalud = afecto;
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre inválido");
        }
    }
}