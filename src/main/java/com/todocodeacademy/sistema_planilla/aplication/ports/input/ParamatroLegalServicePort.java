package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.ParametroLegal;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ParamatroLegalServicePort {

    List<ParametroLegal> findAll();

    ParametroLegal findById(Long id);

    ParametroLegal save(ParametroLegal parametroLegal);

    ParametroLegal update(Long id,ParametroLegal parametroLegal);

    void deleteById(Long id);

    ParametroLegal findVigenteByCodigoAndFecha( String codigo,  LocalDate fecha);

    ParametroLegal findTopByCodigoOrderByFechaInicioVigenciaDesc(String codigo);

    List<ParametroLegal> findByCodigo(String codigo);

    List<ParametroLegal> findByDescripcionContainingIgnoreCase(String descripcion);

    List<ParametroLegal> findByFechaInicioVigenciaBetween(LocalDate startDate, LocalDate endDate);

}