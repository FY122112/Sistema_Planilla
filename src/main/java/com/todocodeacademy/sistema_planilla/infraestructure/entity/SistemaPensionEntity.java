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
@Table(name = "sistema_pension")
public class SistemaPensionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idSistema;

    @Column(nullable = false)
    String nombre;

    @Column(nullable = false)
    String tipo;

    @Column(name = "porcentaje_aporte", precision = 5, scale = 4, nullable = false)
    BigDecimal porcentajeAporte;

    @Column(name = "porcentaje_comision", precision = 5, scale = 4, nullable = false)
    BigDecimal porcentajeComision;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    Instant updatedAt;

    // Protección
    @PrePersist
    public void prePersist() {
        if (porcentajeAporte == null) porcentajeAporte = BigDecimal.ZERO;
        if (porcentajeComision == null) porcentajeComision = BigDecimal.ZERO;
    }
}