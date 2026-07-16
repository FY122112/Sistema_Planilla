package com.todocodeacademy.sistema_planilla.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

// Bitácora de auditoría (HU-012): quién modificó el monto neto de un detalle de planilla
// ya generado, y cuál era el valor antes del cambio. Es un registro de solo-append —no
// tiene reglas de negocio propias más allá de existir— por eso no expone mutadores.
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditoriaCambio {

    Long idAuditoria;

    String usuario;

    Long idPlanilla;
    Long idDetalle;

    BigDecimal montoAnterior;
    BigDecimal montoNuevo;

    Instant fecha;

    private AuditoriaCambio() {}

    public AuditoriaCambio(
            String usuario,
            Long idPlanilla,
            Long idDetalle,
            BigDecimal montoAnterior,
            BigDecimal montoNuevo
    ) {

        if (usuario == null || usuario.isBlank()) {
            throw new IllegalArgumentException("El usuario que realiza el cambio es obligatorio");
        }

        if (idPlanilla == null || idDetalle == null) {
            throw new IllegalArgumentException("La planilla y el detalle afectados son obligatorios");
        }

        this.usuario = usuario;
        this.idPlanilla = idPlanilla;
        this.idDetalle = idDetalle;
        this.montoAnterior = montoAnterior;
        this.montoNuevo = montoNuevo;
        this.fecha = Instant.now();
    }

    public static AuditoriaCambio reconstruir(
            Long idAuditoria,
            String usuario,
            Long idPlanilla,
            Long idDetalle,
            BigDecimal montoAnterior,
            BigDecimal montoNuevo,
            Instant fecha
    ) {

        AuditoriaCambio auditoria = new AuditoriaCambio();

        auditoria.idAuditoria = idAuditoria;
        auditoria.usuario = usuario;
        auditoria.idPlanilla = idPlanilla;
        auditoria.idDetalle = idDetalle;
        auditoria.montoAnterior = montoAnterior;
        auditoria.montoNuevo = montoNuevo;
        auditoria.fecha = fecha;

        return auditoria;
    }
}
