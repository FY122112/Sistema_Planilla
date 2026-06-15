package com.todocodeacademy.sistema_planilla.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "parametros_legales")
public class ParametroLegalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parametro")
    Long idParametro;

    @Column(nullable = false, unique = true, length = 50)
    String codigo;

    @Column(length = 255)
    String descripcion;

    @Column(nullable = false, precision = 10, scale = 4)
    BigDecimal valor;

    @Column(name = "fecha_vigencia_inicio", nullable = false)
    LocalDate fechaInicioVigencia;

    @Column(name = "fecha_vigencia_fin")
    LocalDate fechaFinVigencia;

    // =========================
    // AUDITORÍA
    // =========================

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    Instant updatedAt;

    // =========================
    // VALIDACIONES DEFENSIVAS
    // =========================

    @PrePersist
    @PreUpdate
    private void validate() {

        if (valor == null) {
            valor = BigDecimal.ZERO;
        }

        if (fechaInicioVigencia == null) {
            fechaInicioVigencia = LocalDate.now();
        }

        if (codigo != null) {
            codigo = codigo.trim().toUpperCase();
        }
    }
}