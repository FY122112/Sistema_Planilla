package com.todocodeacademy.sistema_planilla.domain.model;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoAsistencia;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Asistencia {

    Long idAsistencia;
    Empleado empleado;

    LocalDate fecha;
    LocalTime horaEntrada;
    LocalTime horaSalida;

    Integer minutosTardanzas;
    BigDecimal horasExtras25;
    BigDecimal horasExtras10;

    EstadoAsistencia estadoAsistencia;
    String justificacion;

    Instant createdAt;
    Instant updatedAt;

    // =========================
    // 🏗️ CREACIÓN
    // =========================
    public static Asistencia crear(Empleado empleado, LocalDate fecha) {

        if (empleado == null) {
            throw new IllegalArgumentException("Empleado requerido");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("Fecha requerida");
        }

        Asistencia a = new Asistencia();
        a.empleado = empleado;
        a.fecha = fecha;

        a.horasExtras25 = BigDecimal.ZERO;
        a.horasExtras10 = BigDecimal.ZERO;
        a.minutosTardanzas = 0;
        a.estadoAsistencia = EstadoAsistencia.PRESENTE;

        return a;
    }

    // =========================
    // 🔄 RECONSTRUCCIÓN
    // =========================
    public static Asistencia reconstruir(
            Long id,
            Empleado empleado,
            LocalDate fecha,
            LocalTime horaEntrada,
            LocalTime horaSalida,
            Integer minutosTardanzas,
            BigDecimal horasExtras25,
            BigDecimal horasExtras10,
            EstadoAsistencia estado,
            String justificacion,
            Instant createdAt,
            Instant updatedAt
    ) {
        Asistencia a = crear(empleado, fecha);

        a.idAsistencia = id;
        a.horaEntrada = horaEntrada;
        a.horaSalida = horaSalida;

        a.minutosTardanzas = minutosTardanzas != null ? minutosTardanzas : 0;
        a.horasExtras25 = horasExtras25 != null ? horasExtras25 : BigDecimal.ZERO;
        a.horasExtras10 = horasExtras10 != null ? horasExtras10 : BigDecimal.ZERO;

        a.estadoAsistencia = estado != null ? estado : EstadoAsistencia.PRESENTE;
        a.justificacion = justificacion;

        a.createdAt = createdAt;
        a.updatedAt = updatedAt;

        return a;
    }

    // =========================
    // 🧠 LÓGICA DE NEGOCIO
    // =========================

    public void registrarEntrada(LocalTime entrada) {
        if (entrada == null) {
            throw new IllegalArgumentException("Hora entrada requerida");
        }

        if (this.horaEntrada != null) {
            throw new IllegalStateException("Entrada ya registrada");
        }

        this.horaEntrada = entrada;
    }

    public void registrarSalida(LocalTime salida) {
        if (salida == null) {
            throw new IllegalArgumentException("Hora salida requerida");
        }

        if (this.horaEntrada == null) {
            throw new IllegalStateException("No puede registrar salida sin entrada");
        }

        if (this.horaSalida != null) {
            throw new IllegalStateException("Salida ya registrada");
        }

        if (salida.isBefore(horaEntrada)) {
            throw new IllegalArgumentException("Salida no puede ser antes que entrada");
        }

        this.horaSalida = salida;
    }

    public void registrarHorasExtras(BigDecimal horas25, BigDecimal horas10) {
        this.horasExtras25 = horas25 != null ? horas25 : BigDecimal.ZERO;
        this.horasExtras10 = horas10 != null ? horas10 : BigDecimal.ZERO;
    }

    public void marcarTardanza(int minutos) {
        if (minutos < 0) {
            throw new IllegalArgumentException("Minutos inválidos");
        }
        this.minutosTardanzas = minutos;
    }

    public void marcarFalta() {
        this.estadoAsistencia = EstadoAsistencia.FALTA;
    }

    public void justificar(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Motivo requerido");
        }

        this.justificacion = motivo;
        this.estadoAsistencia = EstadoAsistencia.FALTA_JUSTIFICADA;
    }

    // =========================
    // ⏱️ CÁLCULO REAL (DOMINIO)
    // =========================
    public Duration calcularHorasTrabajadas() {
        if (horaEntrada == null || horaSalida == null) {
            return Duration.ZERO;
        }

        if (horaSalida.isBefore(horaEntrada)) {
            return Duration.ZERO;
        }

        return Duration.between(horaEntrada, horaSalida);
    }

    public void calcularTardanza(LocalTime horaInicioJornada) {

        if (horaEntrada == null) {
            return;
        }

        if (horaEntrada.isAfter(horaInicioJornada)) {

            long minutos = Duration
                    .between(horaInicioJornada, horaEntrada)
                    .toMinutes();

            this.minutosTardanzas = (int) minutos;
            this.estadoAsistencia = EstadoAsistencia.TARDANZA;

        } else {

            this.minutosTardanzas = 0;
            this.estadoAsistencia = EstadoAsistencia.PRESENTE;
        }
    }

    public void calcularHorasExtras(LocalTime horaFinJornada) {

        if (horaSalida == null) {
            return;
        }

        if (horaSalida.isAfter(horaFinJornada)) {

            long minutosExtras = Duration
                    .between(horaFinJornada, horaSalida)
                    .toMinutes();

            BigDecimal horas = BigDecimal.valueOf(minutosExtras)
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

            this.horasExtras25 = horas;
        }
    }

    public boolean tieneEntrada() {
        return horaEntrada != null;
    }

    public boolean tieneSalida() {
        return horaSalida != null;
    }


}