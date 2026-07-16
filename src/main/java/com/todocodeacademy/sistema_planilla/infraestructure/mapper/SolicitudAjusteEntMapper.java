package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.SolicitudAjuste;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.SolicitudAjusteEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitudAjusteEntMapper {

    private final EmpleadoEntMapper empleadoMapper;
    private final BoletaEntMapper boletaMapper;

    // =========================
    // ENTITY → DOMAIN
    // =========================

    public SolicitudAjuste toDomain(SolicitudAjusteEntity entity) {

        if (entity == null) {
            return null;
        }

        return SolicitudAjuste.reconstruir(
                entity.getIdSolicitud(),
                empleadoMapper.toDomain(entity.getEmpleado()),
                boletaMapper.toDomain(entity.getBoleta()),
                entity.getMensaje(),
                entity.getEstado(),
                entity.getFechaCreacion(),
                entity.getFechaAtencion()
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================

    // A diferencia de Boleta.detallePlanilla (que en este código base SIEMPRE es una
    // referencia liviana, nunca hidratada), SolicitudAjuste.empleado/boleta llegan aquí ya
    // completamente cargados desde sus repositorios (ver SolicitudAjusteService.crear()).
    // Se reutilizan los mappers completos a propósito: no hay cascade configurado en estas
    // asociaciones (ver SolicitudAjusteEntity), así que Hibernate solo toma el id para la FK
    // y no re-persiste el agregado Empleado/Boleta. Una referencia liviana (solo el id)
    // rompía el re-fetch del adapter: dentro del mismo Session, Hibernate reutilizaba esa
    // instancia a medio poblar desde la caché de primer nivel en vez de recargar los datos
    // reales, y el nombre/apellido nulos reventaban la validación del dominio Empleado.
    public SolicitudAjusteEntity toEntity(SolicitudAjuste domain) {

        if (domain == null) {
            return null;
        }

        SolicitudAjusteEntity entity = new SolicitudAjusteEntity();

        entity.setIdSolicitud(domain.getIdSolicitud());
        entity.setEmpleado(empleadoMapper.toEntity(domain.getEmpleado()));
        entity.setBoleta(boletaMapper.toEntity(domain.getBoleta()));

        entity.setMensaje(domain.getMensaje());
        entity.setEstado(domain.getEstado());
        entity.setFechaAtencion(domain.getFechaAtencion());

        // fecha_creacion es updatable=false (Hibernate la ignora en el UPDATE real), pero
        // si no se copia aquí, la entidad recién construida para un merge (p. ej. al marcar
        // "atendida") queda con fechaCreacion en null en memoria — y por la caché de primer
        // nivel del Session, el re-fetch del adapter puede devolver esa misma instancia sin
        // recargarla, mostrando null en la respuesta aunque la fila en BD nunca lo pierde.
        entity.setFechaCreacion(domain.getFechaCreacion());

        return entity;
    }
}
