package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoCivil;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.RegimenLaboral;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.Sexo;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoDocumento;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateEmpleadoRequest(

        @NotBlank
        String nombre,

        @NotBlank
        String apellido,

        @NotNull
        TipoDocumento tipoDocumento,

        @NotBlank
        String numeroDocumento,

        @NotNull
        LocalDate fechaNacimiento,

        @NotNull
        Sexo sexo,

        @NotNull
        EstadoCivil estadoCivil,

        String nacionalidad,

        @Email
        String correo,

        String direccionCompleta,
        String distrito,
        String provincia,
        String departamento,

        @NotNull
        LocalDate fechaIngreso,

        @NotNull
        Long idPuesto,

        @NotNull
        RegimenLaboral regimenLaboral,

        Boolean tieneHijosCalificados,

        Long idSistemaPension,

        Long idBanco,

        String codigoPension,
        String nombreAfp,
        String numeroCuentaBanco

) {
}