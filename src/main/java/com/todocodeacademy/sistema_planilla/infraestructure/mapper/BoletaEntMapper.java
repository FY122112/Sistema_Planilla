package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.BoletaEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.DetallePlanillaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class BoletaEntMapper {

    // ❌ NO inyectar DetallePlanillaEntMapper aquí

    // =========================
    // ENTITY → DOMAIN
    // =========================
    public Boleta toDomain(BoletaEntity entity){

        if(entity == null){
            return null;
        }

        // 🔥 referencia ligera
        DetallePlanilla detalleRef = null;
        if (entity.getDetallePlanilla() != null) {
            detalleRef = DetallePlanilla.reconstruir(
                    entity.getDetallePlanilla().getIdDetalle(),
                    null, null,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    null,
                    null,
                    null,
                    null
            );
        }

        return Boleta.reconstruir(
                entity.getIdBoleta(),
                detalleRef,
                entity.getFechaEmision(),
                entity.getPeriodoMes(),
                entity.getPeriodoAnio(),
                entity.getSueldoBruto(),
                entity.getTotalDescuento(),
                entity.getSueldoNeto(),
                entity.getRutaPdf(),
                entity.getEstadoBoleta(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public BoletaEntity toEntity(Boleta domain){

        if(domain == null){
            return null;
        }

        BoletaEntity entity = new BoletaEntity();

        entity.setIdBoleta(domain.getIdBoleta());

        // 🔥 referencia ligera
        if (domain.getDetallePlanilla() != null){
            DetallePlanillaEntity detalleRef = new DetallePlanillaEntity();
            detalleRef.setIdDetalle(domain.getDetallePlanilla().getIdDetalle());
            entity.setDetallePlanilla(detalleRef);
        }

        entity.setFechaEmision(domain.getFechaEmision());
        entity.setPeriodoMes(domain.getPeriodoMes());
        entity.setPeriodoAnio(domain.getPeriodoAnio());
        entity.setSueldoBruto(domain.getSueldoBruto());
        entity.setTotalDescuento(domain.getTotalDescuento());
        entity.setSueldoNeto(domain.getSueldoNeto());
        entity.setRutaPdf(domain.getRutaPdf());
        entity.setEstadoBoleta(domain.getEstadoBoleta());

        // created_at es updatable=false (Hibernate lo ignora en el UPDATE), pero si no
        // se copia aquí la entidad recién construida para el merge queda con createdAt en
        // null en memoria, y eso es lo que se refleja en la respuesta justo después de un
        // update (aunque el valor real en la fila de la BD nunca se pierde).
        entity.setCreatedAt(domain.getCreatedAt());

        return entity;
    }
}