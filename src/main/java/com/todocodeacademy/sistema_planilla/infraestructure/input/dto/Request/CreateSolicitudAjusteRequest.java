package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSolicitudAjusteRequest(

        @NotNull
        Long idBoleta,

        @NotBlank
        String mensaje

) {
}
