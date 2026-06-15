package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepositoryPort {

    List<Empleado> findAll();

    Optional<Empleado> findById(Long id);

    Optional<Empleado>findByNumeroDocumento(String numeroDocumento);

    Empleado save(Empleado banco);

    void deleteById(Long id);

    List<Empleado> findByEstado(Boolean estado);

    List<Empleado> findByTieneHijosCalificados(Boolean tieneHijosCalificados);

    List<Empleado> searchByDniOrNameOrLastName(String query);

}
