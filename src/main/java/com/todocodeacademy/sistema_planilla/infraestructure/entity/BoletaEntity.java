package com.todocodeacademy.sistema_planilla.infraestructure.entity;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoBoleta;
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
@Table(
        name = "boleta",
        indexes = {
                @Index(name = "idx_boleta_periodo", columnList = "periodo_anio, periodo_mes")
        }
)
public class BoletaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idBoleta;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_detalle", nullable = false, unique = true)
    DetallePlanillaEntity detallePlanilla;

    @Column(name = "fecha_emision", nullable = false)
    LocalDate fechaEmision;

    @Column(name = "periodo_mes", nullable = false)
    Integer periodoMes;

    @Column(name = "periodo_anio", nullable = false)
    Integer periodoAnio;

    @Column(name = "sueldo_bruto", nullable = false, precision = 10, scale = 2)
    BigDecimal sueldoBruto;

    @Column(name = "total_descuentos", nullable = false, precision = 10, scale = 2)
    BigDecimal totalDescuento;

    @Column(name = "sueldo_neto", nullable = false, precision = 10, scale = 2)
    BigDecimal sueldoNeto;

    @Column(name = "ruta_pdf", length = 255)
    String rutaPdf;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    EstadoBoleta estadoBoleta;

    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    Instant updatedAt;
}