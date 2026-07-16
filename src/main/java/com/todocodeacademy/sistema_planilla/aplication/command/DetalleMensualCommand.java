package com.todocodeacademy.sistema_planilla.aplication.command;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DetalleMensualCommand(
        Integer diasNoLaborados,
        Integer minutosTardanza,
        BigDecimal horasExtras25,
        BigDecimal horasExtras35,
        Integer diasVacacionesGozadas,
        LocalDate vacacionesFechaInicio,
        LocalDate vacacionesFechaFin,
        BigDecimal bonificacionEficiencia,
        BigDecimal comisionComercial
) {
}
