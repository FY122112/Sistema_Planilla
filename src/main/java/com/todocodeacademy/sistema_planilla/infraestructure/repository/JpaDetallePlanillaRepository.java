package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.DetallePlanillaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDetallePlanillaRepository extends JpaRepository<DetallePlanillaEntity,Long> {
}
