package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.ConceptoPago;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.MetodoCalculado;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoSistemaPensiones;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.ConceptoPagoRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.ConceptoPagoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ConceptoPagoMapper {

    public ConceptoPago toDomain(ConceptoPagoRequestDTO request) {

        ConceptoPago concepto = new ConceptoPago(
                request.getCodigoSunat(),
                request.getNombreConcepto(),
                TipoConcepto.fromDisplayName(request.getTipoConcepto()),          // ✅ cambio
                MetodoCalculado.fromDisplayName(request.getMetodoCalculado()),    // ✅ cambio
                request.isEsRemunerativo()
        );

        concepto.actualizarValorReferencial(request.getValorReferencial());

        if (request.getTipoSistemaPensiones() != null) {
            concepto.actualizarTipoSistemaPensiones(
                    TipoSistemaPensiones.fromDisplayName(request.getTipoSistemaPensiones()) // ✅ cambio
            );
        }

        concepto.actualizarAfectoEssalud(request.isAfectoEssalud());
        concepto.actualizarDescripcion(request.getDescripcion());

        return concepto;
    }

    public ConceptoPagoResponseDTO toResponseDTO(ConceptoPago concepto) {
        return new ConceptoPagoResponseDTO(
                concepto.getIdConcepto(),
                concepto.getNombreConcepto(),
                concepto.getTipoConcepto().getDisplayName(),   // ✅ cambio para mostrar legible
                concepto.getValorReferencial(),
                concepto.getDescripcion()
        );
    }
}
