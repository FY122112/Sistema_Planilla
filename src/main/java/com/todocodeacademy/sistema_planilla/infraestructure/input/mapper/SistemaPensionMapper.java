package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.SistemaPension;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.SistemaPensionRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.SistemaPensionResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class SistemaPensionMapper {

    // REQUEST -> ENTITY
    public static SistemaPension toEntity(
            SistemaPensionRequestDTO dto
    ) {

        return new SistemaPension(
                dto.getNombre(),
                dto.getTipo(),
                dto.getPorcentajeAporte(),
                dto.getPorcentajeComision()
        );
    }

    // ENTITY -> RESPONSE
    public static SistemaPensionResponseDTO toResponseDTO(
            SistemaPension entity
    ) {

        return new SistemaPensionResponseDTO(
                entity.getIdSistema(),
                entity.getNombre(),
                entity.getTipo(),
                entity.getPorcentajeAporte(),
                entity.getPorcentajeComision()
        );
    }
}