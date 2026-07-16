package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.aplication.command.DetalleMensualCommand;
import com.todocodeacademy.sistema_planilla.aplication.ports.input.PlanillaServicePort;
import com.todocodeacademy.sistema_planilla.domain.model.Planilla;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.DetalleMensualUpdateRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.PlanillaRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.DetallePlanillaResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.AuditoriaCambioResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.PlanillaResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.ResumenMensualDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.mapper.PlanillaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// La planilla general es exclusiva del Administrador/Contador; un EMPLEADO con sesión de
// autoservicio solo debe ver sus propias boletas, nunca la planilla completa.
@PreAuthorize("hasRole('ADMINISTRADOR')")
@RestController
@RequestMapping("/api/planillas")
@RequiredArgsConstructor
public class PlanillaController {

    private final PlanillaServicePort service;
    private final PlanillaMapper mapper;

    // =========================
    // LISTAR TODAS
    // =========================

    @GetMapping
    public ResponseEntity<List<PlanillaResponseDTO>> findAll() {

        List<PlanillaResponseDTO> response =
                service.findAll()
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    // =========================
    // OBTENER POR ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<PlanillaResponseDTO> findById(
            @PathVariable Long id
    ) {

        PlanillaResponseDTO response =
                mapper.toResponse(
                        service.findById(id)
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // GENERAR PLANILLA
    // =========================

    @PostMapping("/generar")
    public ResponseEntity<PlanillaResponseDTO> generarPlanilla(
            @RequestBody PlanillaRequestDTO request
    ) {

        Planilla planilla =
                service.generarPlanilla(
                        request.getMes(),
                        request.getAnio(),
                        request.getTipoPlanilla()
                );

        PlanillaResponseDTO response =
                mapper.toResponse(planilla);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================
    // CERRAR PLANILLA
    // =========================

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<PlanillaResponseDTO> cerrarPlanilla(
            @PathVariable Long id
    ) {

        PlanillaResponseDTO response =
                mapper.toResponse(
                        service.cerrarPlanilla(id)
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // ABRIR PLANILLA
    // =========================

    @PatchMapping("/{id}/abrir")
    public ResponseEntity<PlanillaResponseDTO> abrirPlanilla(
            @PathVariable Long id
    ) {

        PlanillaResponseDTO response =
                mapper.toResponse(
                        service.abrirPlanilla(id)
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // ELIMINAR PLANILLA
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {

        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    // =========================
    // PREVISUALIZAR DETALLE MENSUAL (sin guardar)
    // =========================

    @PostMapping("/{idPlanilla}/detalles/{idDetalle}/preview")
    public ResponseEntity<DetallePlanillaResponseDTO> previsualizarDetalle(
            @PathVariable Long idPlanilla,
            @PathVariable Long idDetalle,
            @RequestBody DetalleMensualUpdateRequestDTO request
    ) {

        DetallePlanillaResponseDTO response =
                mapper.toDetalleResponse(
                        service.previsualizarDetalleMensual(
                                idPlanilla,
                                idDetalle,
                                toCommand(request)
                        )
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // ACTUALIZAR DETALLE MENSUAL (persiste)
    // =========================

    @PutMapping("/{idPlanilla}/detalles/{idDetalle}")
    public ResponseEntity<DetallePlanillaResponseDTO> actualizarDetalle(
            @PathVariable Long idPlanilla,
            @PathVariable Long idDetalle,
            @RequestBody DetalleMensualUpdateRequestDTO request,
            Authentication authentication
    ) {

        DetallePlanillaResponseDTO response =
                mapper.toDetalleResponse(
                        service.actualizarDetalleMensual(
                                idPlanilla,
                                idDetalle,
                                toCommand(request),
                                authentication.getName()
                        )
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // AUDITORÍA (HU-012)
    // =========================

    @GetMapping("/{idPlanilla}/auditoria")
    public ResponseEntity<List<AuditoriaCambioResponseDTO>> obtenerAuditoria(
            @PathVariable Long idPlanilla
    ) {

        List<AuditoriaCambioResponseDTO> response = service.obtenerAuditoria(idPlanilla).stream()
                .map(a -> AuditoriaCambioResponseDTO.builder()
                        .idAuditoria(a.getIdAuditoria())
                        .usuario(a.getUsuario())
                        .idDetalle(a.getIdDetalle())
                        .montoAnterior(a.getMontoAnterior())
                        .montoNuevo(a.getMontoNuevo())
                        .fecha(a.getFecha())
                        .build())
                .toList();

        return ResponseEntity.ok(response);
    }

    // =========================
    // EXPORTAR A EXCEL
    // =========================

    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> exportarExcel(
            @PathVariable Long id
    ) {

        Planilla planilla = service.findById(id);
        byte[] excel = service.exportarExcel(id);

        String nombreArchivo = "planilla-%s-%s.xlsx".formatted(planilla.getMes(), planilla.getAnio());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(nombreArchivo).build().toString())
                .body(excel);
    }

    // =========================
    // RESUMEN MENSUAL (HU-015)
    // =========================

    @GetMapping("/resumen-mensual")
    public ResponseEntity<List<ResumenMensualDTO>> resumenMensual(
            @RequestParam(defaultValue = "12") int ultimosMeses
    ) {

        List<ResumenMensualDTO> response = service.resumenMensual(ultimosMeses).stream()
                .map(r -> ResumenMensualDTO.builder()
                        .mes(r.mes())
                        .anio(r.anio())
                        .totalNeto(r.totalNeto())
                        .build())
                .toList();

        return ResponseEntity.ok(response);
    }

    private DetalleMensualCommand toCommand(DetalleMensualUpdateRequestDTO request) {

        return new DetalleMensualCommand(
                request.getDiasNoLaborados(),
                request.getMinutosTardanza(),
                request.getHorasExtras25(),
                request.getHorasExtras35(),
                request.getDiasVacacionesGozadas(),
                request.getVacacionesFechaInicio(),
                request.getVacacionesFechaFin(),
                request.getBonificacionEficiencia(),
                request.getComisionComercial()
        );
    }
}