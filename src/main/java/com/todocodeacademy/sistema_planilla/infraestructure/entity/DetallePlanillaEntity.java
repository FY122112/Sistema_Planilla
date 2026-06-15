package com.todocodeacademy.sistema_planilla.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "detalle_planilla")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePlanillaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_planilla", nullable = false)
    private PlanillaEntity planilla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    private EmpleadoEntity empleado;

    @Column(nullable = false)
    private BigDecimal sueldoBase;

    private BigDecimal asignacionFamiliar;

    private BigDecimal remuneracionComputableAfecta;

    private BigDecimal totalIngresosAdicionales;
    private BigDecimal totalDescuento;
    private BigDecimal totalAportesEmpleador;

    private BigDecimal sueldoBruto;
    private BigDecimal sueldoNeto;

    // 🔥 RELACIÓN IMPORTANTE
    @OneToMany(mappedBy = "detallePlanilla", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovimientoPlanillaEntity> movimientosPlanilla;

    @OneToOne(mappedBy = "detallePlanilla", cascade = CascadeType.ALL)
    private BoletaEntity boleta;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}