package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.MovimientoPlanilla;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovimientoPlanillaEntMapper {

    private final ConceptoPagoEntMapper mapperConcepto;

    // =========================
    // ENTITY → DOMAIN
    // =========================
    public MovimientoPlanilla toDomain(MovimientoPlanillaEntity entity) {

        if (entity == null) return null;

        return MovimientoPlanilla.reconstruir(
                entity.getIdMovimiento(),
                null, // ⚠️ evitamos ciclo con DetallePlanilla
                mapperConcepto.toDomain(entity.getConcepto()),
                entity.getMonto(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public MovimientoPlanillaEntity toEntity(MovimientoPlanilla domain) {

        if (domain == null) return null;

        MovimientoPlanillaEntity entity = new MovimientoPlanillaEntity();

        entity.setIdMovimiento(domain.getIdMovimiento());
        entity.setMonto(domain.getMonto());

        // =========================
        // RELACIONES (SOLO ID 🔥)
        // =========================

        if (domain.getDetallePlanilla() != null) {
            DetallePlanillaEntity detalle = new DetallePlanillaEntity();
            detalle.setIdDetalle(domain.getDetallePlanilla().getIdDetalle());
            entity.setDetallePlanilla(detalle);
        }

        if (domain.getConcepto() != null) {
            // A diferencia de detallePlanilla (una referencia real hacia el padre, que
            // igual se recarga completo desde su propio repositorio), acá sí conviene
            // mapear el ConceptoPago completo: los movimientos se re-mapean a dominio
            // justo después de guardar (ver PlanillaRepositoryAdapter.save), y un stub
            // con solo el id dejaría nombreConcepto/tipoConcepto en null en esa vuelta,
            // rompiendo la validación de ConceptoPago.reconstruir(...).
            entity.setConcepto(mapperConcepto.toEntity(domain.getConcepto()));
        }

        return entity;
    }
}