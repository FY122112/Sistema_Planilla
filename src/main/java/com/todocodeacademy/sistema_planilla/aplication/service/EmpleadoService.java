package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.EmpleadoServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.BancoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpleadoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.PuestoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.SistemaPensionRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoService implements EmpleadoServicePort {

    private final EmpleadoRepositoryPort empleadoRepository;

    private final PuestoRepositoryPort puestoRepository;
    private final BancoRepositoryPort bancoRepository;
    private final SistemaPensionRepositoryPort sistemaPensionRepository;

    @Override
    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }

    @Override
    public Empleado findById(Long id) {

        return empleadoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Empleado no encontrado"
                        )
                );
    }

    @Override
    public Empleado save(Empleado empleado) {

        empleadoRepository
                .findByNumeroDocumento(
                        empleado.getNumeroDocumento()
                )
                .ifPresent(e -> {
                    throw new IllegalArgumentException(
                            "Ya existe un empleado con el documento: "
                                    + empleado.getNumeroDocumento()
                    );
                });

        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado update(Long id, Empleado empleado) {

        Empleado actual = findById(id);

        // =========================
        // DATOS BASICOS
        // =========================

        if (empleado.getNombre() != null) {
            actual.actualizarNombre(
                    empleado.getNombre()
            );
        }

        if (empleado.getApellido() != null) {
            actual.actualizarApellido(
                    empleado.getApellido()
            );
        }

        if (empleado.getNumeroDocumento() != null) {

            empleadoRepository
                    .findByNumeroDocumento(
                            empleado.getNumeroDocumento()
                    )
                    .ifPresent(existente -> {

                        if (!existente.getIdEmpleado().equals(id)) {

                            throw new IllegalArgumentException(
                                    "Ya existe un empleado con el documento: "
                                            + empleado.getNumeroDocumento()
                            );
                        }
                    });

            actual.actualizarNumeroDocumento(
                    empleado.getNumeroDocumento()
            );
        }

        if (empleado.getFechaIngreso() != null) {
            actual.actualizarFechaIngreso(
                    empleado.getFechaIngreso()
            );
        }

        // =========================
        // DATOS PERSONALES
        // =========================

        actual.actualizarDatosPersonales(
                empleado.getTipoDocumento(),
                empleado.getFechaNacimiento(),
                empleado.getSexo(),
                empleado.getEstadoCivil(),
                empleado.getNacionalidad()
        );

        // =========================
        // CONTACTO
        // =========================

        if (empleado.getCorreo() != null) {

            actual.actualizarContacto(
                    empleado.getCorreo()
            );
        }

        // =========================
        // DIRECCION
        // =========================

        actual.actualizarDireccion(
                empleado.getDireccionCompleta(),
                empleado.getDistrito(),
                empleado.getProvincia(),
                empleado.getDepartamento()
        );

        // =========================
        // ESTADO LABORAL
        // =========================

        actual.actualizarEstadoLaboral(
                empleado.getEstado(),
                empleado.getFechaCese()
        );

        // =========================
        // REGIMEN LABORAL
        // =========================

        actual.actualizarRegimenLaboral(
                empleado.getRegimenLaboral(),
                empleado.isTieneHijosCalificados()
        );

        // =========================
        // RELACIONES
        // =========================

        if (empleado.getPuesto() != null) {

            actual.asignarPuesto(
                    empleado.getPuesto()
            );
        }

        if (empleado.getBanco() != null) {

            actual.asignarBanco(
                    empleado.getBanco()
            );
        }

        if (empleado.getSistemaPension() != null) {

            actual.asignarSistemaPension(
                    empleado.getSistemaPension()
            );
        }

        // =========================
        // DATOS FINANCIEROS
        // =========================

        actual.actualizarDatosFinancieros(
                empleado.getCodigoPension(),
                empleado.getNombreAfp(),
                empleado.getNumeroCuentaBanco()
        );

        return empleadoRepository.save(actual);
    }

    @Override
    public void deleteById(Long id) {

        findById(id);

        empleadoRepository.deleteById(id);
    }

    @Override
    public List<Empleado> findByEstado(Boolean estado) {

        return empleadoRepository.findByEstado(
                estado
        );
    }

    @Override
    public List<Empleado> findByTieneHijosCalificados(
            Boolean tieneHijosCalificados
    ) {

        return empleadoRepository.findByTieneHijosCalificados(
                tieneHijosCalificados
        );
    }

    @Override
    public List<Empleado> searchByDniOrNameOrLastName(
            String query
    ) {

        return empleadoRepository
                .searchByDniOrNameOrLastName(query);
    }
}