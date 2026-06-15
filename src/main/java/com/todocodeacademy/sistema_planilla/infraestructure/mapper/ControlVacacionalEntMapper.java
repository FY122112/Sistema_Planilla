package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.ControlVacacion;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.ControlVacacionEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpleadoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ControlVacacionalEntMapper {

    private final EmpleadoEntMapper mapperEmpleado;

    // =========================
    // ENTITY → DOMAIN
    // =========================

    public ControlVacacion toDomain(
            ControlVacacionEntity entity
    ) {

        if (entity == null) {
            return null;
        }

        return ControlVacacion.reconstruir(
                entity.getIdControlVacacion(),

                mapperEmpleado.toDomain(
                        entity.getEmpleado()
                ),

                entity.getFechaInicioPeriodo(),

                entity.getFechaFinPeriodo(),

                entity.getDiasGanados(),

                entity.getDiasGozados(),

                entity.getCreatedAt(),

                entity.getUpdatedAt()
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================

    public ControlVacacionEntity toEntity(
            ControlVacacion domain
    ) {

        if (domain == null) {
            return null;
        }

        ControlVacacionEntity entity =
                new ControlVacacionEntity();

        entity.setIdControlVacacion(
                domain.getIdControlVacacion()
        );

        // =========================
        // EMPLEADO
        // =========================

        if (domain.getEmpleado() != null) {

            EmpleadoEntity empleado =
                    new EmpleadoEntity();

            empleado.setIdEmpleado(
                    domain.getEmpleado().getIdEmpleado()
            );

            entity.setEmpleado(empleado);
        }

        // =========================
        // PERIODO
        // =========================

        entity.setFechaInicioPeriodo(
                domain.getFechaInicioPeriodo()
        );

        entity.setFechaFinPeriodo(
                domain.getFechaFinPeriodo()
        );

        // =========================
        // DÍAS
        // =========================

        entity.setDiasGanados(
                domain.getDiasGanados()
        );

        entity.setDiasGozados(
                domain.getDiasGozados()
        );

        // ❌ NO mapear auditoría
        // Hibernate la maneja solo

        return entity;
    }
}