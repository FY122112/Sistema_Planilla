package com.todocodeacademy.sistema_planilla.infraestructure.entity;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoAsistencia;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "asistencia",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_empleado_fecha", columnNames = {"id_empleado", "fecha"})
        },
        indexes = {
                @Index(name = "idx_asistencia_empleado_fecha", columnList = "id_empleado, fecha")
        }
)
public class AsistenciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idAsistencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    EmpleadoEntity empleado;

    @Column(nullable = false)
    LocalDate fecha;

    @Column(name = "hora_entrada")
    LocalTime horaEntrada;

    @Column(name = "hora_salida")
    LocalTime horaSalida;

    @Column(name = "minutos_tardanzas", nullable = false)
    Integer minutosTardanzas;

    @Column(name = "horas_extras_25", precision = 6, scale = 2, nullable = false)
    BigDecimal horasExtras25;

    @Column(name = "horas_extras_10", precision = 6, scale = 2, nullable = false)
    BigDecimal horasExtras10;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_asistencia", nullable = false, length = 20)
    EstadoAsistencia estadoAsistencia;

    @Column(length = 255)
    String justificacion;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    Instant updatedAt;

    @PrePersist
    public void prePersist() {
        if (horasExtras25 == null) horasExtras25 = BigDecimal.ZERO;
        if (horasExtras10 == null) horasExtras10 = BigDecimal.ZERO;
        if (minutosTardanzas == null) minutosTardanzas = 0;
    }

    @PreUpdate
    public void preUpdate() {
        if (horasExtras25 == null) horasExtras25 = BigDecimal.ZERO;
        if (horasExtras10 == null) horasExtras10 = BigDecimal.ZERO;
        if (minutosTardanzas == null) minutosTardanzas = 0;
    }
}