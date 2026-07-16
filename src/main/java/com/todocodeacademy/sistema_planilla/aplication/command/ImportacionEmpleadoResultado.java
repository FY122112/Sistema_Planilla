package com.todocodeacademy.sistema_planilla.aplication.command;

public record ImportacionEmpleadoResultado(
        int fila,
        String numeroDocumento,
        boolean exitoso,
        String mensaje
) {
}
