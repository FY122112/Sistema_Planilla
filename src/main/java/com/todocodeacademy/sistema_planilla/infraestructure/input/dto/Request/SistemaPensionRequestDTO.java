package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SistemaPensionRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 50, message = "El tipo no puede exceder 50 caracteres")
    private String tipo;

    @NotNull(message = "El porcentaje de aporte es obligatorio")
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "El porcentaje de aporte no puede ser negativo"
    )
    private BigDecimal porcentajeAporte;

    @NotNull(message = "El porcentaje de comisión es obligatorio")
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "El porcentaje de comisión no puede ser negativo"
    )
    private BigDecimal porcentajeComision;


}
