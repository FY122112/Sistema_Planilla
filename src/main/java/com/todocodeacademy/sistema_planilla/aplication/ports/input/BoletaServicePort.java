package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.Boleta;

import java.util.List;

public interface BoletaServicePort {

    List<Boleta> findAll();

    Boleta findById(Long id);

    Boleta save(Boleta boleta);

    Boleta update(Long id, Boleta boleta);

    void deleteById(Long id);

    byte[] generarPdf(Long id);

    List<Boleta> findByEmpleadoId(Long idEmpleado);

    Boleta firmarComoEmpleado(Long idBoleta, Long idEmpleadoAutenticado);

    byte[] exportarZip(Integer periodoMes, Integer periodoAnio);

}
