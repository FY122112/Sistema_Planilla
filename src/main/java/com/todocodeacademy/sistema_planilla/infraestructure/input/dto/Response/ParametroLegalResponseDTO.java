package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
public class ParametroLegalResponseDTO {

    private Long idParametro;

    private String codigo;

    private String descripcion;

    private BigDecimal valor;

    private LocalDate fechaInicioVigencia;

    private LocalDate fechaFinVigencia;


}