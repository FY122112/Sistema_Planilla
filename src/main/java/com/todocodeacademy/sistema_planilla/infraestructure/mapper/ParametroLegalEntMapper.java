package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.ParametroLegal;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.ParametroLegalEntity;
import org.springframework.stereotype.Component;

@Component
public class ParametroLegalEntMapper {

    // =========================
    // ENTITY → DOMAIN
    // =========================

    public ParametroLegal toDomain(
            ParametroLegalEntity entity
    ) {

        if (entity == null) {
            return null;
        }

        return ParametroLegal.reconstruir(
                entity.getIdParametro(),
                entity.getCodigo(),
                entity.getDescripcion(),
                entity.getValor(),
                entity.getFechaInicioVigencia(),
                entity.getFechaFinVigencia(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================

    public ParametroLegalEntity toEntity(
            ParametroLegal domain
    ) {

        if (domain == null) {
            return null;
        }

        ParametroLegalEntity entity =
                new ParametroLegalEntity();

        entity.setIdParametro(domain.getIdParametro());
        entity.setCodigo(domain.getCodigo());
        entity.setDescripcion(domain.getDescripcion());
        entity.setValor(domain.getValor());
        entity.setFechaInicioVigencia(
                domain.getFechaInicioVigencia()
        );
        entity.setFechaFinVigencia(
                domain.getFechaFinVigencia()
        );

        return entity;
    }
}