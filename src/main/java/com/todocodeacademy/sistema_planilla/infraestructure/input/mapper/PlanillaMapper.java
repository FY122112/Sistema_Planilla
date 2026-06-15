package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Planilla;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.DetallePlanillaResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.PlanillaResponseDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PlanillaMapper {

    public PlanillaResponseDTO toResponse(Planilla planilla) {

        return PlanillaResponseDTO.builder()
                .idPlanilla(planilla.getIdPlanilla())
                .mes(planilla.getMes())
                .anio(planilla.getAnio())
                .tipoPlanilla(planilla.getTipoPlanilla())
                .fechaGenerada(planilla.getFechaGenerada())
                .cerrada(planilla.isCerrada())
                .cantidadDetalles(
                        planilla.getDetallesPlanilla().size()
                )
                .detalles(
                        planilla.getDetallesPlanilla()
                                .stream()
                                .map(this::toDetalleResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private DetallePlanillaResponseDTO toDetalleResponse(
            DetallePlanilla detalle
    ) {

        return DetallePlanillaResponseDTO.builder()
                .idDetalle(detalle.getIdDetalle())
                .idEmpleado(
                        detalle.getEmpleado().getIdEmpleado()
                )
                .empleado(
                        detalle.getEmpleado().getNombreCompleto()
                )
                .sueldoBase(detalle.getSueldoBase())
                .asignacionFamiliar(
                        detalle.getAsignacionFamiliar()
                )
                .sueldoBruto(detalle.getSueldoBruto())
                .totalDescuento(
                        detalle.getTotalDescuento()
                )
                .sueldoNeto(detalle.getSueldoNeto())
                .build();
    }
}