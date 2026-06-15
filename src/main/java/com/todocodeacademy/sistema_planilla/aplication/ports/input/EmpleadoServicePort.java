package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.Empleado;

import java.util.List;

public interface EmpleadoServicePort {

    List<Empleado> findAll();

    Empleado findById(Long id);

    Empleado save(Empleado empleado);

    Empleado update(Long id, Empleado empleado);

    void deleteById(Long id);

    List<Empleado> findByEstado(Boolean estado);

    List<Empleado> findByTieneHijosCalificados(Boolean tieneHijosCalificados);

    List<Empleado> searchByDniOrNameOrLastName(String query);

}
