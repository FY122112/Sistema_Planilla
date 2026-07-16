package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DetalleMensualUpdateRequestDTO {

    private Integer diasNoLaborados;

    private Integer minutosTardanza;

    private BigDecimal horasExtras25;

    private BigDecimal horasExtras35;

    private Integer diasVacacionesGozadas;

    private LocalDate vacacionesFechaInicio;

    private LocalDate vacacionesFechaFin;

    private BigDecimal bonificacionEficiencia;

    private BigDecimal comisionComercial;
}
