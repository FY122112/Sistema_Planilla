package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
public class AuditoriaCambioResponseDTO {

    private Long idAuditoria;

    private String usuario;

    private Long idDetalle;

    private BigDecimal montoAnterior;
    private BigDecimal montoNuevo;

    private Instant fecha;
}
