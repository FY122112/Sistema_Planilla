package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.DetallePlanillaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.MovimientoPlanilla;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.CreateBoletaRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.UpdateBoletaRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.BoletaResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.MovimientoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BoletaMapper {

    private final DetallePlanillaRepositoryPort detallePlanillaRepository;

    // CREATE
    public Boleta toDomain(CreateBoletaRequest request) {

        DetallePlanilla detalle = detallePlanillaRepository.findById(request.idDetalle())
                .orElseThrow(() ->
                        new IllegalArgumentException("Detalle de planilla no encontrado"));

        return Boleta.crear(
                detalle,
                request.fechaEmision(),
                request.periodoMes(),
                request.periodoAnio(),
                request.sueldoBruto(),
                request.totalDescuento()
        );
    }

    // UPDATE
    // BoletaService.update solo aplica los campos que vengan no-null sobre la boleta
    // existente, así que este payload se arma tal cual llega el request (sin defaults).
    public Boleta toDomain(UpdateBoletaRequest request) {
        return Boleta.reconstruir(
                null, null, null, null, null,
                request.sueldoBruto(), request.totalDescuento(), null,
                request.rutaPdf(), request.estadoBoleta(), null, null, null
        );
    }

    // RESPONSE
    public BoletaResponseDTO toResponse(Boleta boleta) {

        Long idDetalle = boleta.getDetallePlanilla() != null
                ? boleta.getDetallePlanilla().getIdDetalle()
                : null;

        DetallePlanilla detalle = idDetalle != null
                ? detallePlanillaRepository.findById(idDetalle).orElse(null)
                : null;

        BoletaResponseDTO.BoletaResponseDTOBuilder builder = BoletaResponseDTO.builder()
                .idBoleta(boleta.getIdBoleta())
                .idDetalle(idDetalle)
                .fechaEmision(boleta.getFechaEmision())
                .periodoMes(boleta.getPeriodoMes())
                .periodoAnio(boleta.getPeriodoAnio())
                .sueldoBruto(boleta.getSueldoBruto())
                .totalDescuento(boleta.getTotalDescuento())
                .sueldoNeto(boleta.getSueldoNeto())
                .rutaPdf(boleta.getRutaPdf())
                .estadoBoleta(boleta.getEstadoBoleta())
                .fechaFirma(boleta.getFechaFirma())
                .createdAt(boleta.getCreatedAt())
                .updatedAt(boleta.getUpdatedAt());

        if (detalle != null) {

            builder.idEmpleado(detalle.getEmpleado().getIdEmpleado())
                    .empleado(detalle.getEmpleado().getNombreCompleto())
                    .numeroDocumento(detalle.getEmpleado().getNumeroDocumento())
                    .cargo(
                            detalle.getEmpleado().getPuesto() != null
                                    ? detalle.getEmpleado().getPuesto().getNombre()
                                    : null
                    )
                    .nombreBanco(
                            detalle.getEmpleado().getBanco() != null
                                    ? detalle.getEmpleado().getBanco().getNombreBanco()
                                    : null
                    )
                    .numeroCuentaBanco(detalle.getEmpleado().getNumeroCuentaBanco())
                    .nombreSistemaPension(
                            detalle.getEmpleado().getSistemaPension() != null
                                    ? detalle.getEmpleado().getSistemaPension().getNombre()
                                    : null
                    )
                    .telefonoEmpleado(detalle.getEmpleado().getTelefono())
                    .totalAportesEmpleador(detalle.getTotalAportesEmpleador())
                    .diasVacacionesGozadas(detalle.getDiasVacacionesGozadas())
                    .vacacionesFechaInicio(detalle.getVacacionesFechaInicio())
                    .vacacionesFechaFin(detalle.getVacacionesFechaFin())
                    .horasExtras25(detalle.getHorasExtras25())
                    .horasExtras35(detalle.getHorasExtras35())
                    .movimientos(toMovimientoResponses(detalle.obtenerMovimientos()));
        }

        return builder.build();
    }

    private List<MovimientoResponseDTO> toMovimientoResponses(List<MovimientoPlanilla> movimientos) {

        return movimientos.stream()
                .map(mov -> MovimientoResponseDTO.builder()
                        .codigo(mov.getConcepto().getCodigoSunat())
                        .nombreConcepto(mov.getConcepto().getNombreConcepto())
                        .tipoConcepto(mov.getConcepto().getTipoConcepto())
                        .monto(mov.getMonto())
                        .build())
                .collect(Collectors.toList());
    }
}
