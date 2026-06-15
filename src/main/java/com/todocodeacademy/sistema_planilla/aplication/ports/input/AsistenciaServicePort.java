package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.aplication.command.MarcarAsistneciaCommand;
import com.todocodeacademy.sistema_planilla.domain.model.Asistencia;

import java.time.LocalDate;
import java.util.List;

public interface AsistenciaServicePort {

    Asistencia marcarAsistencia(MarcarAsistneciaCommand commnad);

    Asistencia justificarAsistencia(Long id,String motivo);

    List<Asistencia> mostrarReporteDiario(LocalDate fecha);

    List<Asistencia> findAll();

    Asistencia findById(long id);

    Asistencia save(Asistencia asistencia);

    Asistencia update(Long id, Asistencia asistencia );

    void deleteById(Long id);


}
