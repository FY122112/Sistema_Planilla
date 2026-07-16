package com.todocodeacademy.sistema_planilla.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

// Tabla de solo-append: guarda idPlanilla/idDetalle como columnas planas (no @ManyToOne)
// a propósito, para no arrastrar los agregados Planilla/DetallePlanilla completos a un log
// que solo necesita los ids como referencia.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "auditoria_cambio")
public class AuditoriaCambioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idAuditoria;

    @Column(nullable = false, length = 100)
    String usuario;

    @Column(name = "id_planilla", nullable = false)
    Long idPlanilla;

    @Column(name = "id_detalle", nullable = false)
    Long idDetalle;

    @Column(name = "monto_anterior", precision = 10, scale = 2)
    BigDecimal montoAnterior;

    @Column(name = "monto_nuevo", precision = 10, scale = 2)
    BigDecimal montoNuevo;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    Instant fecha;
}
