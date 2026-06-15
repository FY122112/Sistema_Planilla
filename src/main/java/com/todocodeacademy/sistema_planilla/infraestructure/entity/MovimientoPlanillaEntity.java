package com.todocodeacademy.sistema_planilla.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "movimiento_planilla")
public class MovimientoPlanillaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idMovimiento;

    // =========================
    // RELACIONES
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_detalle", nullable = false)
    DetallePlanillaEntity detallePlanilla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_concepto", nullable = false)
    ConceptoPagoEntity concepto;

    // =========================
    // CAMPOS
    // =========================

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal monto;

    // =========================
    // AUDITORÍA
    // =========================

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    Instant updatedAt;
}