package com.todocodeacademy.sistema_planilla.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "control_vacacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ControlVacacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idControlVacacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    private EmpleadoEntity empleado;

    @Column(name = "fecha_inicio_periodo",
            nullable = false)
    LocalDate fechaInicioPeriodo;

    @Column(name = "fecha_fin_periodo",
            nullable = false)
    LocalDate fechaFinPeriodo;

    @Column(nullable = false)
    private Integer diasGanados;

    @Column(nullable = false)
    private Integer diasGozados;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    // =========================
    // 🔒 PROTECCIÓN
    // =========================
    @PrePersist
    public void prePersist() {
        if (diasGanados == null) diasGanados = 0;
        if (diasGozados == null) diasGozados = 0;
    }

    @PreUpdate
    public void preUpdate() {
        if (diasGanados == null) diasGanados = 0;
        if (diasGozados == null) diasGozados = 0;
    }
}