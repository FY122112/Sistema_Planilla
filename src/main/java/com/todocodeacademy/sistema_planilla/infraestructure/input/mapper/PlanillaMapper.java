package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.MovimientoPlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Planilla;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.DetallePlanillaResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.MovimientoResponseDTO;
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

    public DetallePlanillaResponseDTO toDetalleResponse(
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
                .totalAportesEmpleador(
                        detalle.getTotalAportesEmpleador()
                )
                .sueldoNeto(detalle.getSueldoNeto())
                .diasNoLaborados(detalle.getDiasNoLaborados())
                .minutosTardanza(detalle.getMinutosTardanza())
                .horasExtras25(detalle.getHorasExtras25())
                .horasExtras35(detalle.getHorasExtras35())
                .diasVacacionesGozadas(detalle.getDiasVacacionesGozadas())
                .vacacionesFechaInicio(detalle.getVacacionesFechaInicio())
                .vacacionesFechaFin(detalle.getVacacionesFechaFin())
                .bonificacionEficiencia(detalle.getBonificacionEficiencia())
                .comisionComercial(detalle.getComisionComercial())
                .movimientos(
                        detalle.obtenerMovimientos()
                                .stream()
                                .map(this::toMovimientoResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private MovimientoResponseDTO toMovimientoResponse(MovimientoPlanilla movimiento) {

        return MovimientoResponseDTO.builder()
                .codigo(movimiento.getConcepto().getCodigoSunat())
                .nombreConcepto(movimiento.getConcepto().getNombreConcepto())
                .tipoConcepto(movimiento.getConcepto().getTipoConcepto())
                .monto(movimiento.getMonto())
                .build();
    }
}