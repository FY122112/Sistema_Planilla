package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.ControlVacacionalRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.ControlVacacion;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpleadoEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.ControlVacacionalEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaControlVacacionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository

@RequiredArgsConstructor
public class ControlVacacionRepositoryAdapter implements ControlVacacionalRepositoryPort {

    private final JpaControlVacacionalRepository repository;

    private final ControlVacacionalEntMapper mapper;

    @Override
    public List<ControlVacacion> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ControlVacacion> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public ControlVacacion save(ControlVacacion banco) {
        return mapper.toDomain(repository.save(mapper.toEntity(banco)));
    }

    @Override
    public void deleteById(Long id) {
            repository.deleteById(id);
    }

    @Override
    public List<ControlVacacion> findVacacionesPendientes(Empleado empleado, Integer dias) {
        EmpleadoEntity empleadoEntity =
                new EmpleadoEntity();

        empleadoEntity.setIdEmpleado(
                empleado.getIdEmpleado()
        );

        return repository.findVacacionesPendientes(empleadoEntity,dias)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<ControlVacacion> findByEmpleado(Empleado empleado) {

        EmpleadoEntity empleadoEntity =
                new EmpleadoEntity();

        empleadoEntity.setIdEmpleado(
                empleado.getIdEmpleado()
        );

        return repository.findByEmpleado(empleadoEntity)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ControlVacacion> findByEmpleadoAndFechaInicioPeriodoAndFechaFinPeriodo(Empleado empleado, LocalDate inicio, LocalDate fin) {
        EmpleadoEntity empleadoEntity =
                new EmpleadoEntity();

        empleadoEntity.setIdEmpleado(
                empleado.getIdEmpleado()
        );

        return repository.findByEmpleadoAndFechaInicioPeriodoAndFechaFinPeriodo(empleadoEntity,inicio,fin).map(mapper::toDomain);

    }

    @Override
    public Optional<ControlVacacion> findPeriodoActivo(
            Empleado empleado,
            LocalDate fecha
    ) {

        EmpleadoEntity empleadoEntity =
                new EmpleadoEntity();

        empleadoEntity.setIdEmpleado(
                empleado.getIdEmpleado()
        );

        return repository
                .findPeriodoActivo(
                        empleadoEntity,
                        fecha
                )
                .map(mapper::toDomain);
    }

}
