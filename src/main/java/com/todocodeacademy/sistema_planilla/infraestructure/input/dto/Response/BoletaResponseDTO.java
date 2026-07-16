package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoBoleta;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class BoletaResponseDTO {

    private Long idBoleta;

    private Long idDetalle;

    private Long idEmpleado;

    private String empleado;

    private String numeroDocumento;

    private String cargo;

    private String nombreBanco;

    private String numeroCuentaBanco;

    private String nombreSistemaPension;

    private String telefonoEmpleado;

    private LocalDate fechaEmision;

    private Integer periodoMes;

    private Integer periodoAnio;

    private BigDecimal sueldoBruto;

    private BigDecimal totalDescuento;

    private BigDecimal totalAportesEmpleador;

    private BigDecimal sueldoNeto;

    private Integer diasVacacionesGozadas;

    private LocalDate vacacionesFechaInicio;

    private LocalDate vacacionesFechaFin;

    private BigDecimal horasExtras25;

    private BigDecimal horasExtras35;

    private List<MovimientoResponseDTO> movimientos;

    private String rutaPdf;

    private EstadoBoleta estadoBoleta;

    private Instant fechaFirma;

    private Instant createdAt;

    private Instant updatedAt;
}
