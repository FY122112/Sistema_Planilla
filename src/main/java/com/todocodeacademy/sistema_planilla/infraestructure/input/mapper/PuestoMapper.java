package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Puesto;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.PuestoRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.PuestoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PuestoMapper {


    // =========================
    // REQUEST → DOMAIN
    // =========================

    public Puesto toDomain(PuestoRequestDTO dto) {

        if (dto == null) {
            return null;
        }

        return Puesto.reconstruir(
                null,
                dto.getNombre(),
                dto.getSalarioBase(),
                dto.getJornadaLaboralHoras(),
                dto.getHoraInicioJornada(),
                dto.getHoraFinJornada(),
                dto.getDescripcion(),
                null,
                null
        );
    }

    // =========================
    // DOMAIN → RESPONSE
    // =========================

    public PuestoResponseDTO toResponse(Puesto puesto) {

        if (puesto == null) {
            return null;
        }

        return PuestoResponseDTO.builder()
                .idPuesto(puesto.getIdPuesto())
                .nombre(puesto.getNombre())
                .salarioBase(puesto.getSalarioBase())
                .jornadaLaboralHoras(puesto.getJornadaLaboralHoras())
                .horaInicioJornada(puesto.getHoraInicioJornada())
                .horaFinJornada(puesto.getHoraFinJornada())
                .descripcion(puesto.getDescripcion())
                .build();
    }

}
