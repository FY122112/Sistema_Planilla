package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateBoletaRequest(

        @NotNull(message = "El detalle de planilla es obligatorio")
        Long idDetalle,

        @NotNull(message = "La fecha de emisión es obligatoria")
        LocalDate fechaEmision,

        @NotNull(message = "El mes del periodo es obligatorio")
        @Min(value = 1, message = "El mes debe estar entre 1 y 12")
        @Max(value = 12, message = "El mes debe estar entre 1 y 12")
        Integer periodoMes,

        @NotNull(message = "El año del periodo es obligatorio")
        @Positive(message = "El año debe ser positivo")
        Integer periodoAnio,

        @NotNull(message = "El sueldo bruto es obligatorio")
        @DecimalMin(value = "0.0", message = "El sueldo bruto no puede ser negativo")
        BigDecimal sueldoBruto,

        @NotNull(message = "El total de descuentos es obligatorio")
        @DecimalMin(value = "0.0", message = "El total de descuentos no puede ser negativo")
        BigDecimal totalDescuento

) {
}
