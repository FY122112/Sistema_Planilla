package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoSolicitud;
import com.todocodeacademy.sistema_planilla.domain.model.SolicitudAjuste;

import java.util.List;
import java.util.Optional;

public interface SolicitudAjusteRepositoryPort {

    List<SolicitudAjuste> findAll();

    Optional<SolicitudAjuste> findById(Long id);

    SolicitudAjuste save(SolicitudAjuste solicitud);

    List<SolicitudAjuste> findByEmpleadoId(Long idEmpleado);

    long countByEstado(EstadoSolicitud estado);
}
