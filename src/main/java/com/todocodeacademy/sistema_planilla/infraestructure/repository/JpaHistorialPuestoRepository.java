package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.HistorialPuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaHistorialPuestoRepository extends JpaRepository<HistorialPuestoEntity,Long> {

}
