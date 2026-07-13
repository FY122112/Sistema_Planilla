package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConceptoPagoRequestDTO {

    private String codigoSunat;
    private String nombreConcepto;
    private String tipoConcepto;
    private String metodoCalculado;
    private boolean esRemunerativo;
    private BigDecimal valorReferencial;
    private String tipoSistemaPensiones;
    private boolean afectoEssalud;
    private String descripcion;
}