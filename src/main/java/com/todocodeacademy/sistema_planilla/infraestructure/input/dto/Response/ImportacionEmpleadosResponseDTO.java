package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ImportacionEmpleadosResponseDTO {

    private Integer totalFilas;

    private Integer exitosos;

    private Integer fallidos;

    private List<ImportacionEmpleadoResultadoDTO> detalle;
}
