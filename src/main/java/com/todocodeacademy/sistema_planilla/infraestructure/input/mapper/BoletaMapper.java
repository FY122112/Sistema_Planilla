package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.DetallePlanillaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.CreateBoletaRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.UpdateBoletaRequest;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.BoletaResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
                request.rutaPdf(), request.estadoBoleta(), null, null
        );
    }

    // RESPONSE
    public BoletaResponseDTO toResponse(Boleta boleta) {

        Long idDetalle = boleta.getDetallePlanilla() != null
                ? boleta.getDetallePlanilla().getIdDetalle()
                : null;

        String nombreEmpleado = idDetalle != null
                ? detallePlanillaRepository.findById(idDetalle)
                        .map(d -> d.getEmpleado().getNombreCompleto())
                        .orElse(null)
                : null;

        return BoletaResponseDTO.builder()
                .idBoleta(boleta.getIdBoleta())
                .idDetalle(idDetalle)
                .empleado(nombreEmpleado)
                .fechaEmision(boleta.getFechaEmision())
                .periodoMes(boleta.getPeriodoMes())
                .periodoAnio(boleta.getPeriodoAnio())
                .sueldoBruto(boleta.getSueldoBruto())
                .totalDescuento(boleta.getTotalDescuento())
                .sueldoNeto(boleta.getSueldoNeto())
                .rutaPdf(boleta.getRutaPdf())
                .estadoBoleta(boleta.getEstadoBoleta())
                .createdAt(boleta.getCreatedAt())
                .updatedAt(boleta.getUpdatedAt())
                .build();
    }
}
