package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.aplication.command.EmpleadoImportRow;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// Columnas esperadas (con encabezado, que se descarta):
// nombre,apellido,tipoDocumento,numeroDocumento,fechaNacimiento,sexo,estadoCivil,
// nacionalidad,correo,telefono,fechaIngreso,puesto,regimenLaboral,tieneHijosCalificados
//
// Es un split por coma simple (sin soporte de comillas/comas embebidas): ninguna de estas
// columnas espera contener comas en uso normal (nombres, DNI, fechas ISO, nombre de puesto).
@Component
public class EmpleadoCsvParser {

    private static final int NUMERO_COLUMNAS = 14;

    public List<EmpleadoImportRow> parse(MultipartFile archivo) {

        List<EmpleadoImportRow> filas = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {

            String linea = lector.readLine(); // encabezado, se descarta
            int numeroFila = 1;

            while ((linea = lector.readLine()) != null) {
                numeroFila++;

                if (linea.isBlank()) {
                    continue;
                }

                String[] columnas = linea.split(",", -1);
                if (columnas.length < NUMERO_COLUMNAS) {
                    throw new IllegalArgumentException(
                            "La fila " + numeroFila + " tiene menos columnas de las esperadas (" + NUMERO_COLUMNAS + ")"
                    );
                }

                filas.add(new EmpleadoImportRow(
                        numeroFila,
                        columnas[0], columnas[1], columnas[2], columnas[3], columnas[4],
                        columnas[5], columnas[6], columnas[7], columnas[8], columnas[9],
                        columnas[10], columnas[11], columnas[12], columnas[13]
                ));
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("No se pudo leer el archivo CSV", e);
        }

        return filas;
    }
}
