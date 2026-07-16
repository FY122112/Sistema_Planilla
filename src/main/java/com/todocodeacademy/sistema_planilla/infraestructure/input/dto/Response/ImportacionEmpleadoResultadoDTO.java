package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImportacionEmpleadoResultadoDTO {

    private Integer fila;

    private String numeroDocumento;

    private Boolean exitoso;

    private String mensaje;
}
