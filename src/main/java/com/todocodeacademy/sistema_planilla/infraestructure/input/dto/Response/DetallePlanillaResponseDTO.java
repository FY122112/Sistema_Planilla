package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DetallePlanillaResponseDTO {

    private Long idDetalle;

    private Long idEmpleado;

    private String empleado;

    private BigDecimal sueldoBase;

    private BigDecimal asignacionFamiliar;

    private BigDecimal sueldoBruto;

    private BigDecimal totalDescuento;

    private BigDecimal sueldoNeto;
}