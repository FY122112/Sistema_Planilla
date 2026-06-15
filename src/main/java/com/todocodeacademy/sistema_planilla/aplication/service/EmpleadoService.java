package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.EmpleadoServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpleadoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoService implements EmpleadoServicePort {

    private final EmpleadoRepositoryPort empleadoRepository;

    @Override
    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }

    @Override
    public Empleado findById(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Empleado no encontrado"));
    }

    @Override
    public Empleado save(Empleado empleado) {

        empleadoRepository
                .findByNumeroDocumento(empleado.getNumeroDocumento())
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

        findById(id);

        empleadoRepository
                .findByNumeroDocumento(empleado.getNumeroDocumento())
                .ifPresent(existente -> {

                    if (!existente.getIdEmpleado().equals(id)) {
                        throw new IllegalArgumentException(
                                "Ya existe un empleado con el documento: "
                                        + empleado.getNumeroDocumento()
                        );
                    }
                });

        return empleadoRepository.save(empleado);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        empleadoRepository.deleteById(id);
    }

    @Override
    public List<Empleado> findByEstado(Boolean estado) {
        return empleadoRepository.findByEstado(estado);
    }

    @Override
    public List<Empleado> findByTieneHijosCalificados(Boolean tieneHijosCalificados) {
        return empleadoRepository.findByTieneHijosCalificados(tieneHijosCalificados);
    }

    @Override
    public List<Empleado> searchByDniOrNameOrLastName(String query) {
        return empleadoRepository.searchByDniOrNameOrLastName(query);
    }
}