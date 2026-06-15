package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmpresaRequestDTO {

    @NotBlank(message = "La razón social es obligatoria")
    @Size(
            max = 150,
            message = "La razón social no puede exceder 150 caracteres"
    )
    private String razonSocial;

    @NotBlank(message = "El RUC es obligatorio")
    @Pattern(
            regexp = "^[0-9]{11}$",
            message = "El RUC debe tener 11 dígitos"
    )
    private String ruc;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(
            max = 255,
            message = "La dirección no puede exceder 255 caracteres"
    )
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(
            max = 20,
            message = "El teléfono no puede exceder 20 caracteres"
    )
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Size(
            max = 100,
            message = "El correo no puede exceder 100 caracteres"
    )
    private String correo;

}
