package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.MovimientoPlanilla;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.DetallePlanillaEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.MovimientoPlanillaEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.PlanillaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DetallePlanillaEntMapper {

    // 🔥 Eliminamos la dependencia a PlanillaEntMapper
    private final EmpleadoEntMapper mapperEmpleado;
    private final MovimientoPlanillaEntMapper mapperMovimiento;
    private final BoletaEntMapper mapperBoleta;

    public DetallePlanilla toDomain(DetallePlanillaEntity entity) {
        if (entity == null) return null;

        List<MovimientoPlanilla> movimientos = entity.getMovimientosPlanilla() != null
                ? entity.getMovimientosPlanilla()
                .stream()
                .map(mapperMovimiento::toDomain)
                .toList()
                : new ArrayList<>();

        DetallePlanilla detalle = DetallePlanilla.reconstruir(
                entity.getIdDetalle(),
                null, // ⚠️ evitar ciclo con planilla
                mapperEmpleado.toDomain(entity.getEmpleado()),
                entity.getSueldoBase(),
                entity.getAsignacionFamiliar(),
                entity.getRemuneracionComputableAfecta(),
                entity.getTotalIngresosAdicionales(),
                entity.getTotalDescuento(),
                entity.getTotalAportesEmpleador(),
                entity.getSueldoBruto(),
                entity.getSueldoNeto(),
                entity.getDiasNoLaborados(),
                entity.getMinutosTardanza(),
                entity.getHorasExtras25(),
                entity.getHorasExtras35(),
                entity.getDiasVacacionesGozadas(),
                entity.getVacacionesFechaInicio(),
                entity.getVacacionesFechaFin(),
                entity.getBonificacionEficiencia(),
                entity.getComisionComercial(),
                movimientos,
                mapperBoleta.toDomain(entity.getBoleta()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );

        return vincularMovimientos(detalle, movimientos);
    }

    private DetallePlanilla vincularMovimientos(DetallePlanilla detalle, List<MovimientoPlanilla> movimientos) {
        movimientos.forEach(mov -> mov.vincularDetalle(detalle));
        return detalle;
    }

    public DetallePlanillaEntity toEntity(DetallePlanilla domain) {
        if (domain == null) return null;

        DetallePlanillaEntity entity = new DetallePlanillaEntity();
        entity.setIdDetalle(domain.getIdDetalle());

        // solo referencia mínima a la planilla
        PlanillaEntity p = new PlanillaEntity();
        p.setIdPlanilla(domain.getPlanilla().getIdPlanilla());
        entity.setPlanilla(p);

        entity.setEmpleado(mapperEmpleado.toEntity(domain.getEmpleado()));
        entity.setSueldoBase(domain.getSueldoBase());
        entity.setAsignacionFamiliar(domain.getAsignacionFamiliar());
        entity.setRemuneracionComputableAfecta(domain.getRemuneracionComputableAfecta());
        entity.setTotalIngresosAdicionales(domain.getTotalIngresosAdicionales());
        entity.setTotalDescuento(domain.getTotalDescuento());
        entity.setTotalAportesEmpleador(domain.getTotalAportesEmpleador());
        entity.setSueldoBruto(domain.getSueldoBruto());
        entity.setSueldoNeto(domain.getSueldoNeto());
        entity.setDiasNoLaborados(domain.getDiasNoLaborados());
        entity.setMinutosTardanza(domain.getMinutosTardanza());
        entity.setHorasExtras25(domain.getHorasExtras25());
        entity.setHorasExtras35(domain.getHorasExtras35());
        entity.setDiasVacacionesGozadas(domain.getDiasVacacionesGozadas());
        entity.setVacacionesFechaInicio(domain.getVacacionesFechaInicio());
        entity.setVacacionesFechaFin(domain.getVacacionesFechaFin());
        entity.setBonificacionEficiencia(domain.getBonificacionEficiencia());
        entity.setComisionComercial(domain.getComisionComercial());

        // Lista mutable: igual que en PlanillaEntMapper, Hibernate necesita poder mutar
        // esta colección en un merge (orphanRemoval=true al reemplazar los movimientos).
        List<MovimientoPlanillaEntity> movimientos = new ArrayList<>();
        for (MovimientoPlanilla mov : domain.obtenerMovimientos()) {
            MovimientoPlanillaEntity movEntity = mapperMovimiento.toEntity(mov);
            movEntity.setDetallePlanilla(entity); // 🔥 relación clave
            movimientos.add(movEntity);
        }
        entity.setMovimientosPlanilla(movimientos);

        return entity;
    }
}
