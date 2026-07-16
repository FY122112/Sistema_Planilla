package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoSolicitud;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.SolicitudAjusteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSolicitudAjusteRepository extends JpaRepository<SolicitudAjusteEntity, Long> {

    List<SolicitudAjusteEntity> findByEmpleado_IdEmpleadoOrderByFechaCreacionDesc(Long idEmpleado);

    long countByEstado(EstadoSolicitud estado);
}
