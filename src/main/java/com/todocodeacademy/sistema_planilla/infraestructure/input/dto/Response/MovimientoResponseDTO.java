package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class MovimientoResponseDTO {

    private String codigo;

    private String nombreConcepto;

    private TipoConcepto tipoConcepto;

    private BigDecimal monto;
}
