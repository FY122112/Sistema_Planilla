package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Puesto;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.PuestoEntity;
import org.springframework.stereotype.Component;

@Component
public class PuestoEntMapper {

    // =========================
    // ENTITY → DOMAIN
    // =========================

    public Puesto toDomain(PuestoEntity entity) {

        if (entity == null) {
            return null;
        }

        return Puesto.reconstruir(
                entity.getIdPuesto(),
                entity.getNombre(),
                entity.getSalarioBase(),
                entity.getJornadaLaboralHoras(),
                entity.getHoraInicioJornada(),
                entity.getHoraFinJornada(),
                entity.getDescripcion(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================

    public PuestoEntity toEntity(Puesto domain) {

        if (domain == null) {
            return null;
        }

        PuestoEntity entity = new PuestoEntity();

        entity.setIdPuesto(domain.getIdPuesto());
        entity.setNombre(domain.getNombre());
        entity.setSalarioBase(domain.getSalarioBase());
        entity.setJornadaLaboralHoras(domain.getJornadaLaboralHoras());
        entity.setHoraInicioJornada(domain.getHoraInicioJornada());
        entity.setHoraFinJornada(domain.getHoraFinJornada());
        entity.setDescripcion(domain.getDescripcion());

        return entity;
    }
}