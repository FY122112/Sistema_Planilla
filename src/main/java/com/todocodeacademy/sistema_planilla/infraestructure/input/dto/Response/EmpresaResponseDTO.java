package com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmpresaResponseDTO {

    private Long idEmpresa;

    private String razonSocial;

    private String ruc;

    private String direccion;

    private String telefono;

    private String correo;
}
