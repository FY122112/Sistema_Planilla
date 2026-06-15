package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.MovimientoPlanillaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMovimientoPlanillaRepository extends JpaRepository<MovimientoPlanillaEntity,Long> {


}
