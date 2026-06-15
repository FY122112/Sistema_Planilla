package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaRequestDTO {

    private String numeroDocumento;

    // entrada | salida
    private String tipoMarca;

}
