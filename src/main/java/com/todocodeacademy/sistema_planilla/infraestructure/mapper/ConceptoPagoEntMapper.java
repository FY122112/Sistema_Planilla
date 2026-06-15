package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.ConceptoPago;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.ConceptoPagoEntity;
import org.springframework.stereotype.Component;

@Component
public class ConceptoPagoEntMapper {

    // =========================
    // ENTITY → DOMAIN
    // =========================
    public ConceptoPago toDomain(ConceptoPagoEntity entity) {

        if (entity == null) return null;

        return ConceptoPago.reconstruir(
                entity.getIdConcepto(),
                entity.getCodigoSunat(),
                entity.getNombreConcepto(),
                entity.getTipoConcepto(),
                entity.getMetodoCalculado(),
                entity.isEsRemunerativo(),
                entity.getValorReferencial(),
                entity.getTipoSistemaPensiones(),
                entity.isAfectoEssalud(),
                entity.getDescripcion(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public ConceptoPagoEntity toEntity(ConceptoPago domain) {

        if (domain == null) return null;

        ConceptoPagoEntity entity = new ConceptoPagoEntity();

        entity.setIdConcepto(domain.getIdConcepto());
        entity.setCodigoSunat(domain.getCodigoSunat());
        entity.setNombreConcepto(domain.getNombreConcepto());
        entity.setTipoConcepto(domain.getTipoConcepto());
        entity.setMetodoCalculado(domain.getMetodoCalculo()); // 👈 ojo naming
        entity.setEsRemunerativo(domain.isEsRemunerativo());
        entity.setValorReferencial(domain.getValorReferencial());
        entity.setTipoSistemaPensiones(domain.getTipoSistemaPensiones());
        entity.setAfectoEssalud(domain.isAfectoEssalud());
        entity.setDescripcion(domain.getDescripcion());

        // ❗ no mapear auditoría manualmente
        // entity.setCreatedAt(...)
        // entity.setUpdatedAt(...)

        return entity;
    }
}