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

    // ==================================================
    // CREATE REQUEST -> DOMAIN
    // ==================================================

    public Empleado toDomain(CreateEmpleadoRequest request) {

        Puesto puesto = puestoRepository
                .findById(request.idPuesto())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Puesto no encontrado"
                        )
                );

        Banco banco = null;

        if (request.idBanco() != null) {

            banco = bancoRepository
                    .findById(request.idBanco())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Banco no encontrado"
                            )
                    );
        }

        SistemaPension sistemaPension = null;

        if (request.idSistemaPension() != null) {

            sistemaPension = sistemaPensionRepository
                    .findById(request.idSistemaPension())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Sistema de pensión no encontrado"
                            )
                    );
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

        empleado.actualizarContacto(
                request.correo()
        );

        empleado.actualizarDireccion(
                request.direccionCompleta(),
                request.distrito(),
                request.provincia(),
                request.departamento()
        );

        empleado.actualizarRegimenLaboral(
                request.regimenLaboral(),
                Boolean.TRUE.equals(
                        request.tieneHijosCalificados()
                )
        );

        if (banco != null) {
            empleado.asignarBanco(banco);
        }

        if (sistemaPension != null) {
            empleado.asignarSistemaPension(
                    sistemaPension
            );
        }

        empleado.actualizarDatosFinancieros(
                request.codigoPension(),
                request.nombreAfp(),
                request.numeroCuentaBanco()
        );

        return empleado;
    }

    // ==================================================
    // UPDATE REQUEST -> DOMAIN
    // ==================================================

    public Empleado toDomain(UpdateEmpleadoRequest request) {

        Puesto puesto = null;

        if (request.idPuesto() != null) {

            puesto = puestoRepository
                    .findById(request.idPuesto())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Puesto no encontrado"
                            )
                    );
        }

        Banco banco = null;

        if (request.idBanco() != null) {

            banco = bancoRepository
                    .findById(request.idBanco())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Banco no encontrado"
                            )
                    );
        }

        SistemaPension sistemaPension = null;

        if (request.idSistemaPension() != null) {

            sistemaPension = sistemaPensionRepository
                    .findById(request.idSistemaPension())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Sistema de pensión no encontrado"
                            )
                    );
        }

        return Empleado.reconstruir(
                null,
                request.nombre(),
                request.apellido(),
                request.tipoDocumento(),
                request.numeroDocumento(),
                request.fechaNacimiento(),
                request.sexo(),
                request.estadoCivil(),
                request.nacionalidad(),
                request.correo(),
                request.direccionCompleta(),
                request.distrito(),
                request.provincia(),
                request.departamento(),
                request.fechaIngreso(),
                request.estado(),
                request.fechaCese(),
                puesto,
                request.regimenLaboral(),
                Boolean.TRUE.equals(
                        request.tieneHijosCalificados()
                ),
                sistemaPension,
                banco,
                request.codigoPension(),
                request.nombreAfp(),
                request.numeroCuentaBanco(),
                null,
                null
        );
    }

    // ==================================================
    // DOMAIN -> RESPONSE
    // ==================================================

    public EmpleadoResponse toResponse(
            Empleado empleado
    ) {

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

                empleado.getDireccionCompleta(),
                empleado.getDistrito(),
                empleado.getProvincia(),
                empleado.getDepartamento(),

                empleado.getFechaIngreso(),

                empleado.getEstado(),
                empleado.getFechaCese(),

                empleado.getPuesto() != null
                        ? empleado.getPuesto().getIdPuesto()
                        : null,

                empleado.getPuesto() != null
                        ? empleado.getPuesto().getNombre()
                        : null,

                empleado.getRegimenLaboral(),

                empleado.isTieneHijosCalificados(),

                empleado.getSistemaPension() != null
                        ? empleado.getSistemaPension().getIdSistema()
                        : null,

                empleado.getSistemaPension() != null
                        ? empleado.getSistemaPension().getNombre()
                        : null,

                empleado.getBanco() != null
                        ? empleado.getBanco().getIdBanco()
                        : null,

                empleado.getBanco() != null
                        ? empleado.getBanco().getNombreBanco()
                        : null,

                empleado.getCodigoPension(),
                empleado.getNombreAfp(),
                empleado.getNumeroCuentaBanco(),

                empleado.getCreatedAt(),
                empleado.getUpdatedAt()
        );
    }
}
