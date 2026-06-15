package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.HistorialPuesto;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistorialPuestoEntMapper {

    private final EmpleadoEntMapper mapperEmpleado;
    private final PuestoEntMapper mapperPuesto;

    // =========================
    // ENTITY → DOMAIN
    // =========================
    public HistorialPuesto toDomain(HistorialPuestoEntity entity) {

        if (entity == null) return null;

        return HistorialPuesto.reconstruir(
                entity.getIdHistorial(),
                mapperEmpleado.toDomain(entity.getEmpleado()),
                mapperPuesto.toDomain(entity.getPuesto()),
                entity.getFechaInicio(),
                entity.getFechaFin(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public HistorialPuestoEntity toEntity(HistorialPuesto domain) {

        if (domain == null) return null;

        HistorialPuestoEntity entity = new HistorialPuestoEntity();

        entity.setIdHistorial(domain.getIdHistorial());
        entity.setFechaInicio(domain.getFechaInicio());
        entity.setFechaFin(domain.getFechaFin());

        // =========================
        // RELACIONES (SOLO ID 🔥)
        // =========================

        if (domain.getEmpleado() != null) {
            EmpleadoEntity emp = new EmpleadoEntity();
            emp.setIdEmpleado(domain.getEmpleado().getIdEmpleado());
            entity.setEmpleado(emp);
        }

        if (domain.getPuesto() != null) {
            PuestoEntity puesto = new PuestoEntity();
            puesto.setIdPuesto(domain.getPuesto().getIdPuesto());
            entity.setPuesto(puesto);
        }

        return entity;
    }
}
