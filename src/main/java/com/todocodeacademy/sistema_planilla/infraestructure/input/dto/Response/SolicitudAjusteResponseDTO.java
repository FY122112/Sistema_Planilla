package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoSolicitud;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class SolicitudAjusteResponseDTO {

    private Long idSolicitud;

    private Long idEmpleado;
    private String nombreEmpleado;

    private Long idBoleta;
    private Integer periodoMes;
    private Integer periodoAnio;

    private String mensaje;
    private EstadoSolicitud estado;

    private Instant fechaCreacion;
    private Instant fechaAtencion;
}
