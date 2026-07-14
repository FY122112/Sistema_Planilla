package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoBoleta;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record UpdateBoletaRequest(

        EstadoBoleta estadoBoleta,

        String rutaPdf,

        @DecimalMin(value = "0.0", message = "El sueldo bruto no puede ser negativo")
        BigDecimal sueldoBruto,

        @DecimalMin(value = "0.0", message = "El total de descuentos no puede ser negativo")
        BigDecimal totalDescuento

) {
}
