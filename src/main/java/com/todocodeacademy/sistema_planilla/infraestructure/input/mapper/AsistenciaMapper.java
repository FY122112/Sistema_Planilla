package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Asistencia;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.AsistenciaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AsistenciaMapper {
    public AsistenciaResponseDTO toResponse(Asistencia asistencia) {

        return AsistenciaResponseDTO.builder()
                .id(asistencia.getIdAsistencia())

                .empleado(
                        asistencia.getEmpleado().getNombre()
                                + " "
                                + asistencia.getEmpleado().getApellido()
                )

                .fecha(asistencia.getFecha())

                .horaEntrada(asistencia.getHoraEntrada())

                .horaSalida(asistencia.getHoraSalida())

                .minutosTardanzas(asistencia.getMinutosTardanzas())

                .horasExtras25(asistencia.getHorasExtras25())

                .horasExtras10(asistencia.getHorasExtras10())

                .estado(
                        asistencia.getEstadoAsistencia().name()
                )

                .justificacion(asistencia.getJustificacion())

                .build();
    }
}