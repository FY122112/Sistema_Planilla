package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoBoleta;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Builder
public class BoletaResponseDTO {

    private Long idBoleta;

    private Long idDetalle;

    private String empleado;

    private LocalDate fechaEmision;

    private Integer periodoMes;

    private Integer periodoAnio;

    private BigDecimal sueldoBruto;

    private BigDecimal totalDescuento;

    private BigDecimal sueldoNeto;

    private String rutaPdf;

    private EstadoBoleta estadoBoleta;

    private Instant createdAt;

    private Instant updatedAt;
}
