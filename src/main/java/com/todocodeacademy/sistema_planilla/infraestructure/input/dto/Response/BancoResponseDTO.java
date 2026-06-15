package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BancoResponseDTO {

    Long id;
    String nombreBanco;
    String codigoBanco;

}
