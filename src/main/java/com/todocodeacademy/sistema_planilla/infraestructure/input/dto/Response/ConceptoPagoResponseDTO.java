package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ConceptoPagoResponseDTO {

    private Long idConcepto;

    private String nombreConcepto;

    private String tipoConcepto;

    private BigDecimal valorReferencial;

    private String descripcion;
}