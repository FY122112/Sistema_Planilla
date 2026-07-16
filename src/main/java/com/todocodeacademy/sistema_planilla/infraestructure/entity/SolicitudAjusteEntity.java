package com.todocodeacademy.sistema_planilla.infraestructure.entity;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoSolicitud;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "solicitud_ajuste")
public class SolicitudAjusteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idSolicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    EmpleadoEntity empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_boleta", nullable = false)
    BoletaEntity boleta;

    @Column(nullable = false, length = 1000)
    String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    EstadoSolicitud estado;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    Instant fechaCreacion;

    @Column(name = "fecha_atencion")
    Instant fechaAtencion;
}
