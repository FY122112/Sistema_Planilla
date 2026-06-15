package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SistemaPensionResponseDTO {

    private Long idSistema;

    private String nombre;

    private String tipo;

    private BigDecimal porcentajeAporte;

    private BigDecimal porcentajeComision;


}
