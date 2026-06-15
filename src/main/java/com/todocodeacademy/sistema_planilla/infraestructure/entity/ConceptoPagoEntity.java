package com.todocodeacademy.sistema_planilla.infraestructure.entity;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.MetodoCalculado;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoSistemaPensiones;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "concepto_pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConceptoPagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConcepto;

    @Column(nullable = false, unique = true)
    private String codigoSunat;

    @Column(nullable = false)
    private String nombreConcepto;

    @Column(nullable = false)
    private String tipoConcepto;

    @Column(nullable = false)
    private String metodoCalculado;

    @Column(nullable = false)
    private boolean esRemunerativo;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorReferencial;

    private String tipoSistemaPensiones;

    @Column(nullable = false)
    private boolean afectoEssalud;

    private String descripcion;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}