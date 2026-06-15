package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Empresa;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpresaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
public class EmpresaEntMapper {
    // =========================
    // ENTITY → DOMAIN
    // =========================
    public Empresa toDomain(EmpresaEntity entity) {

        if (entity == null) return null;

        return Empresa.reconstruir(
                entity.getIdEmpresa(),
                entity.getRazonSocial(),
                entity.getRuc(),
                entity.getDireccion(),
                entity.getTelefono(),
                entity.getCorreo(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public EmpresaEntity toEntity(Empresa domain) {

        if (domain == null) return null;

        EmpresaEntity entity = new EmpresaEntity();

        entity.setIdEmpresa(domain.getIdEmpresa());
        entity.setRazonSocial(domain.getRazonSocial());
        entity.setRuc(domain.getRuc());
        entity.setDireccion(domain.getDireccion());
        entity.setTelefono(domain.getTelefono());
        entity.setCorreo(domain.getCorreo());

        return entity;
    }
}
