package com.todocodeacademy.sistema_planilla.infraestructure.entity;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoCivil;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.RegimenLaboral;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.Sexo;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoDocumento;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "empleado")
public class EmpleadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idEmpleado;

    @Column(nullable = false)
    String nombre;

    @Column(nullable = false)
    String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    TipoDocumento tipoDocumento;

    @Column(name = "numero_documento", nullable = false, unique = true)
    String numeroDocumento;

    @Column(name = "fecha_nacimiento")
    LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil")
    EstadoCivil estadoCivil;

    String nacionalidad;

    String correo;

    @Column(name = "direccion_completa")
    String direccionCompleta;

    String distrito;
    String provincia;
    String departamento;

    @Column(name = "fecha_ingreso", nullable = false)
    LocalDate fechaIngreso;

    @Column(nullable = false)
    boolean estado;

    @Column(name = "fecha_cese")
    LocalDate fechaCese;

    // =========================
    // RELACIONES
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_puesto")
    PuestoEntity puesto;

    @Enumerated(EnumType.STRING)
    @Column(name = "regimen_laboral")
    RegimenLaboral regimenLaboral;

    @Column(nullable = false)
    boolean tieneHijosCalificados;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sistema_pension")
    SistemaPensionEntity sistemaPension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_banco")
    BancoEntity banco;

    @Column(name = "codigo_pension")
    String codigoPension;

    String nombreAfp;

    @Column(name = "numero_cuenta_banco")
    String numeroCuentaBanco;

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