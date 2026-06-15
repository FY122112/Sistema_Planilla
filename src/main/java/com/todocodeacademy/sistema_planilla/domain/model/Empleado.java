package com.todocodeacademy.sistema_planilla.domain.model;


import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoCivil;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.RegimenLaboral;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.Sexo;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoDocumento;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Empleado {

    // ==========================================
    // IDENTIDAD
    // ==========================================

    Long idEmpleado;

    // ==========================================
    // DATOS PERSONALES
    // ==========================================

    String nombre;
    String apellido;

    TipoDocumento tipoDocumento;
    String numeroDocumento;

    LocalDate fechaNacimiento;
    Sexo sexo;
    EstadoCivil estadoCivil;

    String nacionalidad;
    String correo;

    String direccionCompleta;
    String distrito;
    String provincia;
    String departamento;

    // ==========================================
    // DATOS LABORALES
    // ==========================================

    LocalDate fechaIngreso;

    Boolean estado;
    LocalDate fechaCese;

    Puesto puesto;

    RegimenLaboral regimenLaboral;
    boolean tieneHijosCalificados;

    // ==========================================
    // DATOS FINANCIEROS
    // ==========================================

    SistemaPension sistemaPension;
    Banco banco;

    String codigoPension;
    String nombreAfp;
    String numeroCuentaBanco;

    // ==========================================
    // AUDITORIA
    // ==========================================

    Instant createdAt;
    Instant updatedAt;

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public Empleado(
            String nombre,
            String apellido,
            String numeroDocumento,
            LocalDate fechaIngreso,
            Puesto puesto
    ) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }

        if (numeroDocumento == null || numeroDocumento.isBlank()) {
            throw new IllegalArgumentException("El número de documento es obligatorio");
        }

        if (fechaIngreso == null) {
            throw new IllegalArgumentException("La fecha de ingreso es obligatoria");
        }

        if (puesto == null) {
            throw new IllegalArgumentException("El puesto es obligatorio");
        }

        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroDocumento = numeroDocumento;
        this.fechaIngreso = fechaIngreso;
        this.puesto = puesto;

        this.estado = true;
    }

    // ==========================================
    // RECONSTRUCCION
    // ==========================================

    public static Empleado reconstruir(
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
            String direccionCompleta,
            String distrito,
            String provincia,
            String departamento,
            LocalDate fechaIngreso,
            Boolean estado,
            LocalDate fechaCese,
            Puesto puesto,
            RegimenLaboral regimenLaboral,
            boolean tieneHijosCalificados,
            SistemaPension sistemaPension,
            Banco banco,
            String codigoPension,
            String nombreAfp,
            String numeroCuentaBanco,
            Instant createdAt,
            Instant updatedAt
    ) {

        Empleado empleado = new Empleado(
                nombre,
                apellido,
                numeroDocumento,
                fechaIngreso,
                puesto
        );

        empleado.idEmpleado = idEmpleado;

        empleado.tipoDocumento = tipoDocumento;
        empleado.fechaNacimiento = fechaNacimiento;
        empleado.sexo = sexo;
        empleado.estadoCivil = estadoCivil;

        empleado.nacionalidad = nacionalidad;
        empleado.correo = correo;

        empleado.direccionCompleta = direccionCompleta;
        empleado.distrito = distrito;
        empleado.provincia = provincia;
        empleado.departamento = departamento;

        empleado.estado = estado;
        empleado.fechaCese = fechaCese;

        empleado.regimenLaboral = regimenLaboral;
        empleado.tieneHijosCalificados = tieneHijosCalificados;

        empleado.sistemaPension = sistemaPension;
        empleado.banco = banco;

        empleado.codigoPension = codigoPension;
        empleado.nombreAfp = nombreAfp;
        empleado.numeroCuentaBanco = numeroCuentaBanco;

        empleado.createdAt = createdAt;
        empleado.updatedAt = updatedAt;

        return empleado;
    }

    // ==========================================
    // REGLAS DE NEGOCIO
    // ==========================================

    public void cesarEmpleado(LocalDate fechaCese) {

        if (fechaCese == null) {
            throw new IllegalArgumentException("La fecha de cese es obligatoria");
        }

        if (!estaActivo()) {
            throw new IllegalStateException("El empleado ya se encuentra cesado");
        }

        this.estado = false;
        this.fechaCese = fechaCese;
    }

    public void reactivarEmpleado() {

        this.estado = true;
        this.fechaCese = null;
    }

    public boolean estaActivo() {
        return Boolean.TRUE.equals(this.estado);
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    // ==========================================
    // RELACIONES
    // ==========================================

    public void asignarPuesto(Puesto puesto) {

        if (puesto == null) {
            throw new IllegalArgumentException("El puesto no puede ser nulo");
        }

        this.puesto = puesto;
    }

    public void asignarBanco(Banco banco) {

        if (banco == null) {
            throw new IllegalArgumentException("El banco no puede ser nulo");
        }

        this.banco = banco;
    }

    public void asignarSistemaPension(SistemaPension sistemaPension) {

        if (sistemaPension == null) {
            throw new IllegalArgumentException("El sistema de pensión no puede ser nulo");
        }

        this.sistemaPension = sistemaPension;
    }

    // ==========================================
    // ACTUALIZACIONES
    // ==========================================

    public void actualizarDatosPersonales(
            TipoDocumento tipoDocumento,
            LocalDate fechaNacimiento,
            Sexo sexo,
            EstadoCivil estadoCivil,
            String nacionalidad
    ) {

        this.tipoDocumento = tipoDocumento;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.estadoCivil = estadoCivil;
        this.nacionalidad = nacionalidad;
    }

    public void actualizarContacto(String correo) {
        this.correo = correo;
    }

    public void actualizarDireccion(
            String direccionCompleta,
            String distrito,
            String provincia,
            String departamento
    ) {

        this.direccionCompleta = direccionCompleta;
        this.distrito = distrito;
        this.provincia = provincia;
        this.departamento = departamento;
    }

    public void actualizarRegimenLaboral(
            RegimenLaboral regimenLaboral,
            boolean tieneHijosCalificados
    ) {

        this.regimenLaboral = regimenLaboral;
        this.tieneHijosCalificados = tieneHijosCalificados;
    }

    public void actualizarEstadoLaboral(
            Boolean estado,
            LocalDate fechaCese
    ) {

        this.estado = estado;
        this.fechaCese = fechaCese;
    }

    public void actualizarDatosFinancieros(
            String codigoPension,
            String nombreAfp,
            String numeroCuentaBanco
    ) {

        this.codigoPension = codigoPension;
        this.nombreAfp = nombreAfp;
        this.numeroCuentaBanco = numeroCuentaBanco;
    }
    public void actualizarNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        this.nombre = nombre;
    }

    public void actualizarApellido(String apellido) {

        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }

        this.apellido = apellido;
    }

    public void actualizarNumeroDocumento(String numeroDocumento) {

        if (numeroDocumento == null || numeroDocumento.isBlank()) {
            throw new IllegalArgumentException("El número de documento es obligatorio");
        }

        this.numeroDocumento = numeroDocumento;
    }

    public void actualizarFechaIngreso(LocalDate fechaIngreso) {

        if (fechaIngreso == null) {
            throw new IllegalArgumentException("La fecha de ingreso es obligatoria");
        }

        this.fechaIngreso = fechaIngreso;
    }



}