package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Banco;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.BancoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
public class BancoEntMapper {

    // =========================
    // ENTITY → DOMAIN
    // =========================
    public Banco toDomain(BancoEntity entity) {
        if (entity == null) return null;

        return Banco.reconstruir(
                entity.getIdBanco(),
                entity.getNombreBanco(),
                entity.getCodigoBanco(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public BancoEntity toEntity(Banco domain) {
        if (domain == null) return null;

        BancoEntity entity = new BancoEntity();

        entity.setIdBanco(domain.getIdBanco());
        entity.setNombreBanco(domain.getNombreBanco());
        entity.setCodigoBanco(domain.getCodigoBanco());

        return entity;
    }
}