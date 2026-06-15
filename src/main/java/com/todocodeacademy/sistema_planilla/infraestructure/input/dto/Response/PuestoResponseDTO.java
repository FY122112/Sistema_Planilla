package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Builder
public class PuestoResponseDTO {

    private Long idPuesto;

    private String nombre;

    private BigDecimal salarioBase;

    private Integer jornadaLaboralHoras;

    private LocalTime horaInicioJornada;

    private LocalTime horaFinJornada;

    private String descripcion;


}
