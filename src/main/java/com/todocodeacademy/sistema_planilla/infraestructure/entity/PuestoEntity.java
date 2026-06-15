package com.todocodeacademy.sistema_planilla.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "puesto")
public class PuestoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idPuesto;

    @Column(nullable = false, length = 100)
    String nombre;

    @Column(name = "salario_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal salarioBase; // Usar BigDecimal para precisión monetaria

    @Column(name = "jornada_laboral_horas")
    private Integer jornadaLaboralHoras; // Horas de la jornada laboral (ej. 8)

    @Column(name = "hora_inicio_jornada") // Nueva columna para la hora de inicio de la jornada
    private LocalTime horaInicioJornada;

    @Column(name = "hora_fin_jornada") // Nueva columna para la hora de fin de la jornada
    private LocalTime horaFinJornada;

    @Column(name = "descripcion", columnDefinition = "TEXT") // TEXT para descripciones largas
    private String descripcion;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    Instant updatedAt;
}