package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.PuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface JpaPuestoRepository extends JpaRepository<PuestoEntity, Long> {

    Optional<PuestoEntity> findByNombrePuesto(String nombrePuesto);

    /**
     * Busca todos los puestos con un salario base mayor o igual al valor especificado.
     * @param salarioBase El salario base mínimo.
     * @return Una lista de puestos que cumplen con la condición.
     */
    List<PuestoEntity> findBySalarioBaseGreaterThanEqual(BigDecimal salarioBase);

}
