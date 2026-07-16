package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnlaceCompartidoResponseDTO {

    private String token;

    // Ruta relativa (sin host): el frontend antepone su propia constante de base URL del
    // backend, igual que hace con el resto de endpoints.
    private String rutaDescarga;
}
