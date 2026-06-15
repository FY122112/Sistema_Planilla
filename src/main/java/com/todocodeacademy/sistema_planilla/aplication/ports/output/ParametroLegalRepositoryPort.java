package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.ConceptoPago;
import com.todocodeacademy.sistema_planilla.domain.model.ParametroLegal;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.ParametroLegalEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ParametroLegalRepositoryPort {


    List<ParametroLegal> findAll();

    Optional<ParametroLegal> findById(Long id);

    ParametroLegal save(ParametroLegal banco);

    void deleteById(Long id);

    // Busca un parámetro legal por su código y que esté vigente en una fecha dada.
    Optional<ParametroLegal> findVigenteByCodigoAndFecha( String codigo, LocalDate fecha);

    // Método para obtener el valor más reciente de un parámetro por su código
    Optional<ParametroLegal> findTopByCodigoOrderByFechaInicioVigenciaDesc(String codigo); // ADICIONADO


    List<ParametroLegal> findByCodigo(String codigo);

    List<ParametroLegal> findByDescripcionContainingIgnoreCase(String descripcion);

    List<ParametroLegal> findByFechaInicioVigenciaBetween(LocalDate startDate, LocalDate endDate);

}
