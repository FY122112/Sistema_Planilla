package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    private BigDecimal totalAportesEmpleador;

    private BigDecimal sueldoNeto;

    private Integer diasNoLaborados;

    private Integer minutosTardanza;

    private BigDecimal horasExtras25;

    private BigDecimal horasExtras35;

    private Integer diasVacacionesGozadas;

    private LocalDate vacacionesFechaInicio;

    private LocalDate vacacionesFechaFin;

    private BigDecimal bonificacionEficiencia;

    private BigDecimal comisionComercial;

    private List<MovimientoResponseDTO> movimientos;
}