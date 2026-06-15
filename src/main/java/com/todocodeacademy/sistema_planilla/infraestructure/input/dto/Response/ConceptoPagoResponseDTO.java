package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import java.math.BigDecimal;


public record ConceptoPagoResponseDTO(String nombreConcepto, BigDecimal valorReferencial, String descripcion) {
}
