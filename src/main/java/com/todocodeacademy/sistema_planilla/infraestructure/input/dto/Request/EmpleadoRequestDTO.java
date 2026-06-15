package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoRequestDTO {

    // =========================
    // DATOS PERSONALES
    // =========================

    private String nombre;

    private String apellido;

    private String tipoDocumento;

    private String numeroDocumento;

    private LocalDate fechaNacimiento;

    private String sexo;

    private String estadoCivil;

    private String nacionalidad;

    // =========================
    // CONTACTO
    // =========================

    private String correo;

    private String direccionCompleta;

    private String distrito;

    private String provincia;

    private String departamento;

    // =========================
    // LABORALES
    // =========================

    private LocalDate fechaIngreso;

    private String regimenLaboral;

    private boolean tieneHijosCalificados;

    // =========================
    // RELACIONES
    // =========================

    private Long idPuesto;

    private Long idBanco;

    private Long idSistemaPension;

    // =========================
    // DATOS BANCARIOS/PENSIÓN
    // =========================

    private String codigoPension;

    private String nombreAfp;

    private String numeroCuentaBanco;

}
