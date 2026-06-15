package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.ConceptoPago;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.ConceptoPagoRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.ConceptoPagoResponseDTO;

public class ConceptoPagoMapper {

    public ConceptoPago toDomain(
            ConceptoPagoRequestDTO request
    ) {

        ConceptoPago concepto = new ConceptoPago(
                request.getCodigoSunat(),
                request.getNombreConcepto(),
                request.getTipoConcepto(),
                request.getMetodoCalculo(),
                request.isEsRemunerativo()
        );

        concepto.actualizarValorReferencial(
                request.getValorReferencial()
        );

        concepto.actualizarTipoSistemaPensiones(
                request.getTipoSistemaPensiones()
        );

        concepto.actualizarAfectoEssalud(
                request.isAfectoEssalud()
        );

        concepto.actualizarDescripcion(
                request.getDescripcion()
        );

        return concepto;
    }
    public ConceptoPagoResponseDTO toResponseDTO(ConceptoPago concepto) {
        return new ConceptoPagoResponseDTO(
                concepto.getNombreConcepto(),
                concepto.getValorReferencial(),
                concepto.getDescripcion()
        );
    }
}