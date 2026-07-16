package com.todocodeacademy.sistema_planilla.aplication.command;

import java.math.BigDecimal;

public record ResumenMensual(
        Integer mes,
        Integer anio,
        BigDecimal totalNeto
) {
}
