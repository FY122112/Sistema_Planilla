package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoCivil;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.RegimenLaboral;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.Sexo;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoDocumento;

import java.time.LocalDate;

public record UpdateEmpleadoRequest(

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

        RegimenLaboral regimenLaboral,
        Boolean tieneHijosCalificados,

        Long idSistemaPension,

        Long idBanco,

        String codigoPension,
        String nombreAfp,
        String numeroCuentaBanco

) {
}
