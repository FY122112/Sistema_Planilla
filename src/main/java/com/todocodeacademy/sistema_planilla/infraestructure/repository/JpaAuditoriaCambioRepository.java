package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.AuditoriaCambioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaAuditoriaCambioRepository extends JpaRepository<AuditoriaCambioEntity, Long> {

    List<AuditoriaCambioEntity> findByIdPlanillaOrderByFechaDesc(Long idPlanilla);
}
