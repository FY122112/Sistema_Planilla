package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class PuestoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotNull(message = "El salario base es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "El salario debe ser mayor a 0")
    private BigDecimal salarioBase;

    @Positive(message = "La jornada laboral debe ser positiva")
    private Integer jornadaLaboralHoras;

    private LocalTime horaInicioJornada;

    private LocalTime horaFinJornada;

    @Size(max = 1000)
    private String descripcion;

}
