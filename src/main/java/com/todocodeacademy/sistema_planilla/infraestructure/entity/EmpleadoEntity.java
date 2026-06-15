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

    // =========================
    // DATOS PERSONALES
    // =========================

    @Column(nullable = false, length = 100)
    String nombre;

    @Column(nullable = false, length = 100)
    String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    TipoDocumento tipoDocumento;

    @Column(name = "numero_documento", nullable = false, unique = true, length = 20)
    String numeroDocumento;

    @Column(name = "fecha_nacimiento", nullable = false)
    LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil", nullable = false)
    EstadoCivil estadoCivil;

    @Column(length = 50)
    String nacionalidad;

    @Column(length = 100)
    String correo;

    @Column(name = "direccion_completa", length = 255)
    String direccionCompleta;

    @Column(length = 100)
    String distrito;

    @Column(length = 100)
    String provincia;

    @Column(length = 100)
    String departamento;

    // =========================
    // DATOS LABORALES
    // =========================

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
    @JoinColumn(name = "id_puesto", nullable = false)
    PuestoEntity puesto;

    @Enumerated(EnumType.STRING)
    @Column(name = "regimen_laboral", nullable = false)
    RegimenLaboral regimenLaboral;

    @Column(name = "tiene_hijos_calificados", nullable = false)
    boolean tieneHijosCalificados;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sistema_pension")
    SistemaPensionEntity sistemaPension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_banco")
    BancoEntity banco;

    @Column(name = "codigo_pension", length = 50)
    String codigoPension;

    @Column(name = "nombre_afp", length = 100)
    String nombreAfp;

    @Column(name = "numero_cuenta_banco", length = 30)
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

    // =========================
    // DEFAULTS
    // =========================

    @PrePersist
    public void prePersist() {
        this.estado = true;   // empleado activo al crearse
    }
}