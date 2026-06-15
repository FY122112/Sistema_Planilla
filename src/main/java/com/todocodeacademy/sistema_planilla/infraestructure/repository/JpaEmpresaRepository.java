package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaEmpresaRepository extends JpaRepository<EmpresaEntity,Long> {

    /**
     * Busca una empresa por su número de RUC.
     * @param ruc El número de RUC de la empresa.
     * @return La empresa encontrada o null si no existe.
     */
    Optional<EmpresaEntity> findByRuc(String ruc);

}
