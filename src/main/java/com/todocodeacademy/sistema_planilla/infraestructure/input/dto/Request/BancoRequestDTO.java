package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BancoRequestDTO {

    String nombreBanco;
    String codigoBanco;

}
