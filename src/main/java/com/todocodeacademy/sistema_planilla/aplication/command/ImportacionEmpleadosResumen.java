package com.todocodeacademy.sistema_planilla.aplication.command;

import java.util.List;

public record ImportacionEmpleadosResumen(
        int totalFilas,
        int exitosos,
        int fallidos,
        List<ImportacionEmpleadoResultado> detalle
) {
}
