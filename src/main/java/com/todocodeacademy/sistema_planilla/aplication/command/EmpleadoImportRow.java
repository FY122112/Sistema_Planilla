package com.todocodeacademy.sistema_planilla.aplication.command;

// Todos los campos llegan como texto crudo (tal cual la celda del CSV): la conversión a
// tipos (enum, fecha, etc.) y su validación ocurre fila por fila en el servicio, para que
// una celda mal formada no aborte el resto del archivo — solo esa fila queda marcada como
// fallida en el resumen (ver HU-028).
public record EmpleadoImportRow(
        int numeroFila,
        String nombre,
        String apellido,
        String tipoDocumento,
        String numeroDocumento,
        String fechaNacimiento,
        String sexo,
        String estadoCivil,
        String nacionalidad,
        String correo,
        String telefono,
        String fechaIngreso,
        String puesto,
        String regimenLaboral,
        String tieneHijosCalificados
) {
}
