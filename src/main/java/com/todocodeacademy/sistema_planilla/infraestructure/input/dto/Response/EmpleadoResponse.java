package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoCivil;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.RegimenLaboral;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.Sexo;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoDocumento;

import java.time.Instant;
import java.time.LocalDate;

public record EmpleadoResponse(

        Long idEmpleado,

        String nombre,
        String apellido,

        TipoDocumento tipoDocumento,
        String numeroDocumento,

        LocalDate fechaNacimiento,
        Sexo sexo,
        EstadoCivil estadoCivil,

        String nacionalidad,
        String correo,
        String telefono,

        String direccionCompleta,
        String distrito,
        String provincia,
        String departamento,

        LocalDate fechaIngreso,

        Boolean estado,
        LocalDate fechaCese,

        Long idPuesto,
        String nombrePuesto,

        RegimenLaboral regimenLaboral,

        boolean  tieneHijosCalificados,

        Long idSistemaPension,
        String nombreSistemaPension,

        Long idBanco,
        String nombreBanco,

        String codigoPension,
        String nombreAfp,
        String numeroCuentaBanco,

        Instant createdAt,
        Instant updatedAt

) {
}
