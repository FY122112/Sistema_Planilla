package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.ParametroLegalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaParametroLegalRepository extends JpaRepository<ParametroLegalEntity,Long> {

    // Busca un parámetro legal por su código y que esté vigente en una fecha dada.
    @Query("SELECT pl FROM ParametroLegal pl WHERE pl.codigo = :codigo AND pl.fechaInicioVigencia <= :fecha " +
            "AND (pl.fechaFinVigencia IS NULL OR pl.fechaFinVigencia >= :fecha)")
    Optional<ParametroLegalEntity> findVigenteByCodigoAndFecha(@Param("codigo") String codigo, @Param("fecha") LocalDate fecha);

    // Método para obtener el valor más reciente de un parámetro por su código
    Optional<ParametroLegalEntity> findTopByCodigoOrderByFechaInicioVigenciaDesc(String codigo); // ADICIONADO

    List<ParametroLegalEntity> findByCodigo(String codigo);
    List<ParametroLegalEntity> findByDescripcionContainingIgnoreCase(String descripcion);
    List<ParametroLegalEntity> findByFechaInicioVigenciaBetween(LocalDate startDate, LocalDate endDate);

}
