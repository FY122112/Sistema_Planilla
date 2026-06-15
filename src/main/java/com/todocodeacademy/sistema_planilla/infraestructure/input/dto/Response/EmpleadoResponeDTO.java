package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoResponeDTO {
    Long id;
    String nombre;
    String apellido;
    String nombreCompleto;
    String numeroDocumento;
    Boolean estado;
    String puesto;
    LocalDate fechaIngreso;

}
