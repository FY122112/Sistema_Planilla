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

        // Se reconstruyen los detalles antes que la planilla (sin referencia al padre
        // todavía, para evitar el ciclo planilla <-> detalle).
        List<DetallePlanilla> detalles =
                entity.getDetallesPlanilla() != null
                        ? entity.getDetallesPlanilla()
                                .stream()
                                .map(detalleMapper::toDomain)
                                .toList()
                        : new ArrayList<>();

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

        // No se usa agregarDetalle(...) aquí: esa regla de negocio bloquea la
        // operación cuando la planilla está cerrada, pero recargar detalles ya
        // persistidos de una planilla cerrada debe funcionar siempre.
        for (DetallePlanilla d : detalles) {
            d.vincularPlanilla(planilla);
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
            // Lista mutable: Hibernate necesita poder mutar esta colección en un merge
            // (p.ej. al cerrar una planilla ya persistida). Stream.toList() devuelve una
            // lista inmutable y provoca UnsupportedOperationException en ese escenario.
            List<DetallePlanillaEntity> detalles = new ArrayList<>(domain.getDetallesPlanilla()
                    .stream()
                    .map(det -> {
                        DetallePlanillaEntity d = detalleMapper.toEntity(det);
                        d.setPlanilla(entity); // 🔥 relación clave
                        return d;
                    })
                    .toList());

            entity.setDetallesPlanilla(detalles);
        }

        return entity;
    }
}
