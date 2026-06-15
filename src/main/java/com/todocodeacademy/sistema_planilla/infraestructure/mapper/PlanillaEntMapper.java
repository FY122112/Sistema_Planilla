package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Planilla;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.DetallePlanillaEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.PlanillaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlanillaEntMapper {

    private final ParametroLegalEntMapper parametroLegalMapper;
    private final DetallePlanillaEntMapper detalleMapper;

    // =========================
    // ENTITY → DOMAIN
    // =========================
    public Planilla toDomain(PlanillaEntity entity) {
        if (entity == null) return null;

        List<DetallePlanilla> detalles = new ArrayList<>();

        Planilla planilla = Planilla.reconstruir(
                entity.getIdPlanilla(),
                entity.getMes(),
                entity.getAnio(),
                entity.getTipoPlanilla(),
                entity.getFechaGenerada(),
                parametroLegalMapper.toDomain(entity.getParametroLegal()),
                detalles,
                entity.isCerrada(),
                entity.getCreatedAt(),
                entity.getUpdateAt()
        );

        // 👇 después de crear la planilla, agregamos los detalles
        if (entity.getDetallesPlanilla() != null) {
            for (DetallePlanillaEntity det : entity.getDetallesPlanilla()) {
                DetallePlanilla d = detalleMapper.toDomain(det);
                planilla.agregarDetalle(d);
            }
        }

        return planilla;
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public PlanillaEntity toEntity(Planilla domain) {
        if (domain == null) return null;

        PlanillaEntity entity = new PlanillaEntity();
        entity.setIdPlanilla(domain.getIdPlanilla());
        entity.setMes(domain.getMes());
        entity.setAnio(domain.getAnio());
        entity.setTipoPlanilla(domain.getTipoPlanilla());
        entity.setFechaGenerada(domain.getFechaGenerada());
        entity.setParametroLegal(parametroLegalMapper.toEntity(domain.getParametroLegal()));
        entity.setCerrada(domain.estaCerrada());

        if (domain.getDetallesPlanilla() != null) {
            List<DetallePlanillaEntity> detalles = domain.getDetallesPlanilla()
                    .stream()
                    .map(det -> {
                        DetallePlanillaEntity d = detalleMapper.toEntity(det);
                        d.setPlanilla(entity); // 🔥 relación clave
                        return d;
                    })
                    .toList();

            entity.setDetallesPlanilla(detalles);
        }

        return entity;
    }
}
