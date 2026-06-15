package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoPlanilla;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class PlanillaResponseDTO {

    private Long idPlanilla;

    private Integer mes;

    private Integer anio;

    private TipoPlanilla tipoPlanilla;

    private LocalDate fechaGenerada;

    private boolean cerrada;

    private Integer cantidadDetalles;

    private List<DetallePlanillaResponseDTO> detalles;
}