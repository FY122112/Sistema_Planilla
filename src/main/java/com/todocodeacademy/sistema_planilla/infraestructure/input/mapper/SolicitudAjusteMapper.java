package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.SolicitudAjuste;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.SolicitudAjusteResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class SolicitudAjusteMapper {

    public SolicitudAjusteResponseDTO toResponse(SolicitudAjuste solicitud) {

        return SolicitudAjusteResponseDTO.builder()
                .idSolicitud(solicitud.getIdSolicitud())
                .idEmpleado(solicitud.getEmpleado().getIdEmpleado())
                .nombreEmpleado(solicitud.getEmpleado().getNombreCompleto())
                .idBoleta(solicitud.getBoleta().getIdBoleta())
                .periodoMes(solicitud.getBoleta().getPeriodoMes())
                .periodoAnio(solicitud.getBoleta().getPeriodoAnio())
                .mensaje(solicitud.getMensaje())
                .estado(solicitud.getEstado())
                .fechaCreacion(solicitud.getFechaCreacion())
                .fechaAtencion(solicitud.getFechaAtencion())
                .build();
    }
}
