package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ParametroLegalRequestDTO {

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 50,
            message = "El código no puede exceder 50 caracteres")
    private String codigo;

    @Size(max = 255,
            message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;

    @NotNull(message = "El valor es obligatorio")
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "El valor no puede ser negativo"
    )
    private BigDecimal valor;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicioVigencia;

    private LocalDate fechaFinVigencia;
}