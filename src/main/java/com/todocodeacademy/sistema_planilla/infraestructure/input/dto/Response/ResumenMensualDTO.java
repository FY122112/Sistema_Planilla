package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ResumenMensualDTO {

    private Integer mes;

    private Integer anio;

    private BigDecimal totalNeto;
}
