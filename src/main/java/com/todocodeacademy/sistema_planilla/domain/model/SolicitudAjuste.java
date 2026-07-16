package com.todocodeacademy.sistema_planilla.domain.model;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoSolicitud;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

// Buzón de reclamos del empleado sobre su boleta (HU-059): un ticket de texto libre que el
// área de contabilidad atiende desde el panel de administración, no una revisión automática
// de los cálculos.
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SolicitudAjuste {

    Long idSolicitud;

    Empleado empleado;
    Boleta boleta;

    String mensaje;

    EstadoSolicitud estado;

    Instant fechaCreacion;
    Instant fechaAtencion;

    private SolicitudAjuste() {}

    public SolicitudAjuste(Empleado empleado, Boleta boleta, String mensaje) {

        if (empleado == null) {
            throw new IllegalArgumentException("El empleado es obligatorio");
        }

        if (boleta == null) {
            throw new IllegalArgumentException("La boleta es obligatoria");
        }

        if (mensaje == null || mensaje.isBlank()) {
            throw new IllegalArgumentException("El mensaje del reclamo es obligatorio");
        }

        this.empleado = empleado;
        this.boleta = boleta;
        this.mensaje = mensaje;
        this.estado = EstadoSolicitud.PENDIENTE;
        this.fechaCreacion = Instant.now();
    }

    public static SolicitudAjuste reconstruir(
            Long idSolicitud,
            Empleado empleado,
            Boleta boleta,
            String mensaje,
            EstadoSolicitud estado,
            Instant fechaCreacion,
            Instant fechaAtencion
    ) {

        SolicitudAjuste solicitud = new SolicitudAjuste();

        solicitud.idSolicitud = idSolicitud;
        solicitud.empleado = empleado;
        solicitud.boleta = boleta;
        solicitud.mensaje = mensaje;
        solicitud.estado = estado;
        solicitud.fechaCreacion = fechaCreacion;
        solicitud.fechaAtencion = fechaAtencion;

        return solicitud;
    }

    public void marcarComoAtendida() {

        if (this.estado == EstadoSolicitud.ATENDIDA) {
            throw new IllegalStateException("La solicitud ya fue atendida");
        }

        this.estado = EstadoSolicitud.ATENDIDA;
        this.fechaAtencion = Instant.now();
    }

    public boolean estaPendiente() {
        return this.estado == EstadoSolicitud.PENDIENTE;
    }
}
