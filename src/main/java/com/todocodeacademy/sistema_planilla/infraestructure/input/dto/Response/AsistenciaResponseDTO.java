package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaResponseDTO {

    private Long id;

    private String empleado;

    private LocalDate fecha;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaEntrada;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaSalida;

    private Integer minutosTardanzas;

    private BigDecimal horasExtras25;

    private BigDecimal horasExtras10;

    private String estado;

    private String justificacion;
}