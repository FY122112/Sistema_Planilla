package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.SistemaPensionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSistemaPension extends JpaRepository<SistemaPensionEntity, Long> {
}
