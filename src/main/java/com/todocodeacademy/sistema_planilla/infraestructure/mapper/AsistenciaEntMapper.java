package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Asistencia;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.AsistenciaEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpleadoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AsistenciaEntMapper {

    private final EmpleadoEntMapper mapperEmpleado;

    // =========================
    // ENTITY → DOMAIN
    // =========================
    public Asistencia toDomain(AsistenciaEntity entity){

        if (entity == null){
            return null;
        }

        return Asistencia.reconstruir(
                entity.getIdAsistencia(),
                mapperEmpleado.toDomain(entity.getEmpleado()),
                entity.getFecha(),
                entity.getHoraEntrada(),
                entity.getHoraSalida(),
                entity.getMinutosTardanzas(),
                entity.getHorasExtras25(),
                entity.getHorasExtras10(),
                entity.getEstadoAsistencia(),
                entity.getJustificacion(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public AsistenciaEntity toEntity(Asistencia domain){

        if (domain == null) return null;

        AsistenciaEntity entity = new AsistenciaEntity();

        entity.setIdAsistencia(domain.getIdAsistencia());

        // 🔥 RELACIÓN: solo referencia por ID (correcto)
        if (domain.getEmpleado() != null) {
            EmpleadoEntity empRef = new EmpleadoEntity();
            empRef.setIdEmpleado(domain.getEmpleado().getIdEmpleado());
            entity.setEmpleado(empRef);
        }

        entity.setFecha(domain.getFecha());
        entity.setHoraEntrada(domain.getHoraEntrada());
        entity.setHoraSalida(domain.getHoraSalida());

        // 🔥 confiar en dominio (no duplicar lógica)
        entity.setMinutosTardanzas(domain.getMinutosTardanzas());
        entity.setHorasExtras25(domain.getHorasExtras25());
        entity.setHorasExtras10(domain.getHorasExtras10());

        entity.setEstadoAsistencia(domain.getEstadoAsistencia());
        entity.setJustificacion(domain.getJustificacion());

        return entity;
    }
}