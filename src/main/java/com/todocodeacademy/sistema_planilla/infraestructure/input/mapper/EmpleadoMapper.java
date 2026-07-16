package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.BancoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.PuestoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.SistemaPensionRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Banco;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.domain.model.Puesto;
import com.todocodeacademy.sistema_planilla.domain.model.SistemaPension;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.CreateEmpleadoRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.UpdateEmpleadoRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.EmpleadoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmpleadoMapper {

    private final PuestoRepositoryPort puestoRepository;
    private final BancoRepositoryPort bancoRepository;
    private final SistemaPensionRepositoryPort sistemaPensionRepository;

    // CREATE
    public Empleado toDomain(CreateEmpleadoRequest request) {

        Puesto puesto = puestoRepository.findById(request.idPuesto())
                .orElseThrow(() ->
                        new IllegalArgumentException("Puesto no encontrado"));

        Banco banco = null;
        if (request.idBanco() != null) {
            banco = bancoRepository.findById(request.idBanco())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Banco no encontrado"));
        }

        SistemaPension sistemaPension = null;
        if (request.idSistemaPension() != null) {
            sistemaPension = sistemaPensionRepository
                    .findById(request.idSistemaPension())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Sistema pension no encontrado"));
        }

        Empleado empleado = new Empleado(
                request.nombre(),
                request.apellido(),
                request.numeroDocumento(),
                request.fechaIngreso(),
                puesto
        );

        empleado.actualizarDatosPersonales(
                request.tipoDocumento(),
                request.fechaNacimiento(),
                request.sexo(),
                request.estadoCivil(),
                request.nacionalidad()
        );

        empleado.actualizarContacto(request.correo());
        empleado.actualizarTelefono(request.telefono());

        empleado.actualizarDireccion(
                request.direccionCompleta(),
                request.distrito(),
                request.provincia(),
                request.departamento()
        );

        empleado.actualizarRegimenLaboral(
                request.regimenLaboral(),
                Boolean.TRUE.equals(request.tieneHijosCalificados())
        );

        if (banco != null) {
            empleado.asignarBanco(banco);
        }

        if (sistemaPension != null) {
            empleado.asignarSistemaPension(sistemaPension);
        }

        empleado.actualizarDatosFinancieros(
                request.codigoPension(),
                request.nombreAfp(),
                request.numeroCuentaBanco()
        );

        return empleado;
    }

    // UPDATE
    public Empleado updateDomain(UpdateEmpleadoRequest request, Empleado actual) {

        if (request.nombre() != null) {
            actual.actualizarNombre(request.nombre());
        }

        if (request.apellido() != null) {
            actual.actualizarApellido(request.apellido());
        }

        if (request.numeroDocumento() != null) {
            actual.actualizarNumeroDocumento(request.numeroDocumento());
        }

        if (request.fechaIngreso() != null) {
            actual.actualizarFechaIngreso(request.fechaIngreso());
        }

        // PERSONALES
        actual.actualizarDatosPersonales(
                request.tipoDocumento() != null ? request.tipoDocumento() : actual.getTipoDocumento(),
                request.fechaNacimiento() != null ? request.fechaNacimiento() : actual.getFechaNacimiento(),
                request.sexo() != null ? request.sexo() : actual.getSexo(),
                request.estadoCivil() != null ? request.estadoCivil() : actual.getEstadoCivil(),
                request.nacionalidad() != null ? request.nacionalidad() : actual.getNacionalidad()
        );

        // CONTACTO
        if (request.correo() != null) {
            actual.actualizarContacto(request.correo());
        }

        if (request.telefono() != null) {
            actual.actualizarTelefono(request.telefono());
        }

        // DIRECCION
        if (request.direccionCompleta() != null) {
            actual.actualizarDireccion(
                    request.direccionCompleta(),
                    request.distrito(),
                    request.provincia(),
                    request.departamento()
            );
        }

        // ESTADO LABORAL
        if (request.estado() != null) {
            actual.actualizarEstadoLaboral(
                    request.estado(),
                    request.fechaCese()
            );
        }

        // REGIMEN
        if (request.regimenLaboral() != null || request.tieneHijosCalificados() != null) {
            actual.actualizarRegimenLaboral(
                    request.regimenLaboral() != null
                            ? request.regimenLaboral()
                            : actual.getRegimenLaboral(),
                    request.tieneHijosCalificados() != null
                            ? request.tieneHijosCalificados()
                            : actual.isTieneHijosCalificados()
            );
        }

        // PUESTO
        if (request.idPuesto() != null) {
            Puesto puesto = puestoRepository.findById(request.idPuesto())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Puesto no encontrado"));

            actual.asignarPuesto(puesto);
        }

        // BANCO
        if (request.idBanco() != null) {
            Banco banco = bancoRepository.findById(request.idBanco())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Banco no encontrado"));

            actual.asignarBanco(banco);
        }

        // PENSION
        if (request.idSistemaPension() != null) {
            SistemaPension pension = sistemaPensionRepository
                    .findById(request.idSistemaPension())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Sistema pension no encontrado"));

            actual.asignarSistemaPension(pension);
        }

        // FINANCIERO
        if (request.codigoPension() != null ||
                request.nombreAfp() != null ||
                request.numeroCuentaBanco() != null) {

            actual.actualizarDatosFinancieros(
                    request.codigoPension() != null
                            ? request.codigoPension()
                            : actual.getCodigoPension(),

                    request.nombreAfp() != null
                            ? request.nombreAfp()
                            : actual.getNombreAfp(),

                    request.numeroCuentaBanco() != null
                            ? request.numeroCuentaBanco()
                            : actual.getNumeroCuentaBanco()
            );
        }

        return actual;
    }

    // RESPONSE
    public EmpleadoResponse toResponse(Empleado empleado) {
        return new EmpleadoResponse(
                empleado.getIdEmpleado(),
                empleado.getNombre(),
                empleado.getApellido(),
                empleado.getTipoDocumento(),
                empleado.getNumeroDocumento(),
                empleado.getFechaNacimiento(),
                empleado.getSexo(),
                empleado.getEstadoCivil(),
                empleado.getNacionalidad(),
                empleado.getCorreo(),
                empleado.getTelefono(),
                empleado.getDireccionCompleta(),
                empleado.getDistrito(),
                empleado.getProvincia(),
                empleado.getDepartamento(),
                empleado.getFechaIngreso(),
                empleado.getEstado(),
                empleado.getFechaCese(),

                empleado.getPuesto() != null ? empleado.getPuesto().getIdPuesto() : null,
                empleado.getPuesto() != null ? empleado.getPuesto().getNombre() : null,

                empleado.getRegimenLaboral(),
                empleado.isTieneHijosCalificados(),

                empleado.getSistemaPension() != null ? empleado.getSistemaPension().getIdSistema() : null,
                empleado.getSistemaPension() != null ? empleado.getSistemaPension().getNombre() : null,

                empleado.getBanco() != null ? empleado.getBanco().getIdBanco() : null,
                empleado.getBanco() != null ? empleado.getBanco().getNombreBanco() : null,

                empleado.getCodigoPension(),
                empleado.getNombreAfp(),
                empleado.getNumeroCuentaBanco(),
                empleado.getCreatedAt(),
                empleado.getUpdatedAt()
        );
    }
}