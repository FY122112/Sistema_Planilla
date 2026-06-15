package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.ParametroLegal;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.ParametroLegalRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.ParametroLegalResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ParametroLegalMapper {

    // =========================
    // REQUEST → DOMAIN
    // =========================

    public ParametroLegal toDomain(
            ParametroLegalRequestDTO dto
    ) {

        if (dto == null) {
            return null;
        }

        return ParametroLegal.reconstruir(
                null,
                dto.getCodigo(),
                dto.getDescripcion(),
                dto.getValor(),
                dto.getFechaInicioVigencia(),
                dto.getFechaFinVigencia(),
                null,
                null
        );
    }

    // =========================
    // DOMAIN → RESPONSE
    // =========================

    public ParametroLegalResponseDTO toResponse(
            ParametroLegal domain
    ) {

        if (domain == null) {
            return null;
        }

        return ParametroLegalResponseDTO.builder()
                .idParametro(domain.getIdParametro())
                .codigo(domain.getCodigo())
                .descripcion(domain.getDescripcion())
                .valor(domain.getValor())
                .fechaInicioVigencia(
                        domain.getFechaInicioVigencia()
                )
                .fechaFinVigencia(
                        domain.getFechaFinVigencia()
                )
                .build();
    }
}