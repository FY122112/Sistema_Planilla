package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoPlanilla;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanillaRequestDTO {

    private Integer mes;

    private Integer anio;

    private TipoPlanilla tipoPlanilla;
}