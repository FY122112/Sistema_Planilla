package com.todocodeacademy.sistema_planilla.infraestructure.entity;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoPlanilla;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "planilla")
public class PlanillaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idPlanilla;

    Integer mes;
    Integer anio;

    TipoPlanilla tipoPlanilla;

    LocalDate fechaGenerada;

    @ManyToOne(fetch = FetchType.LAZY)
    ParametroLegalEntity parametroLegal;

    @OneToMany(mappedBy = "planilla", cascade = CascadeType.ALL, orphanRemoval = true)
    List<DetallePlanillaEntity> detallesPlanilla;

    boolean cerrada;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    Instant updateAt;
}