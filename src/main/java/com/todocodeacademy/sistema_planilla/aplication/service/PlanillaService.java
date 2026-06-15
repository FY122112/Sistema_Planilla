package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.PlanillaServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpleadoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.ParametroLegalRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.PlanillaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.*;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoPlanilla;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanillaService implements PlanillaServicePort {

    private final PlanillaRepositoryPort planillaRepo;
    private final EmpleadoRepositoryPort empleadoRepo;
    private final ParametroLegalRepositoryPort parametroRepo;

    // =========================
    // CRUD
    // =========================

    @Override
    public List<Planilla> findAll() {
        return planillaRepo.findAll();
    }

    @Override
    public Planilla findById(Long id) {

        return planillaRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Planilla no encontrada"));
    }

    @Override
    public Planilla save(Planilla planilla) {
        return planillaRepo.save(planilla);
    }

    @Override
    public Planilla update(Long id, Planilla planilla) {

        Planilla actual = findById(id);

        if (actual.estaCerrada()) {
            throw new IllegalStateException(
                    "No se puede modificar una planilla cerrada"
            );
        }

        return planillaRepo.save(actual);
    }

    @Override
    public void deleteById(Long id) {

        Planilla planilla = findById(id);

        if (planilla.estaCerrada()) {
            throw new IllegalStateException(
                    "No se puede eliminar una planilla cerrada"
            );
        }

        planillaRepo.deleteById(id);
    }

    // =========================
    // BÚSQUEDAS
    // =========================

    @Override
    public Planilla findByMesAnioAndTipo(
            Integer mes,
            Integer anio,
            TipoPlanilla tipoPlanilla
    ) {

        return planillaRepo
                .findByMesAndAnioAndTipoPlanilla(
                        mes,
                        anio,
                        tipoPlanilla
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "No existe planilla para ese periodo"
                        )
                );
    }

    @Override
    public List<Planilla> findByFechaGeneradaBetween(
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {

        return planillaRepo.findByFechaGeneradaBetween(
                fechaInicio,
                fechaFin
        );
    }

    @Override
    public List<Planilla> findByAnioAndTipo(
            Integer anio,
            TipoPlanilla tipoPlanilla
    ) {

        return planillaRepo.findByAnioAndTipoPlanilla(
                anio,
                tipoPlanilla
        );
    }

    // =========================
    // GENERAR PLANILLA
    // =========================

    @Override
    public Planilla generarPlanilla(
            Integer mes,
            Integer anio,
            TipoPlanilla tipoPlanilla
    ) {

        // =========================
        // VALIDAR EXISTENCIA
        // =========================

        planillaRepo.findByMesAndAnioAndTipoPlanilla(
                mes,
                anio,
                tipoPlanilla
        ).ifPresent(planilla -> {

            throw new IllegalStateException(
                    "La planilla ya existe"
            );
        });

        // =========================
        // OBTENER EMPLEADOS ACTIVOS
        // =========================

        List<Empleado> empleados =
                empleadoRepo.findByEstado(true);

        if (empleados.isEmpty()) {

            throw new RuntimeException(
                    "No existen empleados activos"
            );
        }

        // =========================
        // OBTENER PARÁMETRO RMV
        // =========================

        ParametroLegal parametroRmv =
                parametroRepo
                        .findTopByCodigoOrderByFechaInicioVigenciaDesc("RMV")
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No existe parámetro RMV"
                                )
                        );

        // =========================
        // OBTENER ASIGNACIÓN FAMILIAR
        // =========================

        ParametroLegal parametroAsignacion =
                parametroRepo
                        .findTopByCodigoOrderByFechaInicioVigenciaDesc(
                                "ASIGNACION_FAMILIAR"
                        )
                        .orElse(null);

        // =========================
        // CREAR PLANILLA
        // =========================

        Planilla planilla =
                new Planilla(
                        mes,
                        anio,
                        tipoPlanilla,
                        parametroRmv
                );

        // =========================
        // GENERAR DETALLES
        // =========================

        for (Empleado empleado : empleados) {

            BigDecimal sueldoBase =
                    empleado.getPuesto() != null
                            ? empleado.getPuesto().getSalarioBase()
                            : BigDecimal.ZERO;

            // =========================
            // CREAR DETALLE
            // =========================

            DetallePlanilla detalle =
                    new DetallePlanilla(
                            planilla,
                            empleado,
                            sueldoBase
                    );

            // =========================
            // ASIGNACIÓN FAMILIAR
            // =========================

            if (
                    Boolean.TRUE.equals(
                            empleado.isTieneHijosCalificados()
                    )
                            &&
                            parametroAsignacion != null
            ) {

                detalle.actualizarAsignacionFamiliar(
                        parametroAsignacion.getValor()
                );
            }

            // =========================
            // DESCUENTO AFP / ONP
            // =========================

            SistemaPension sistema =
                    empleado.getSistemaPension();

            if (sistema != null) {

                BigDecimal descuentoPension =
                        sistema.calcularDescuento(
                                sueldoBase
                        );

                ConceptoPago conceptoPension =
                        new ConceptoPago(
                                "AFP_ONP",
                                "Descuento Pensionario",
                                "DESCUENTO",
                                "PORCENTAJE",
                                false
                        );

                MovimientoPlanilla movimientoPension =
                        new MovimientoPlanilla(
                                detalle,
                                conceptoPension,
                                descuentoPension
                        );

                detalle.agregarMovimiento(
                        movimientoPension
                );
            }

            // =========================
            // REMUNERACIÓN COMPUTABLE
            // =========================

            detalle.actualizarRemuneracionComputable(
                    detalle.getSueldoBruto()
            );

            // =========================
            // RECALCULAR TOTALES
            // =========================

            detalle.recalcularTotales();

            // =========================
            // AGREGAR DETALLE
            // =========================

            planilla.agregarDetalle(detalle);
        }

        // =========================
        // GUARDAR PLANILLA
        // =========================

        return planillaRepo.save(planilla);
    }

    // =========================
    // CERRAR PLANILLA
    // =========================

    @Override
    public Planilla cerrarPlanilla(Long idPlanilla) {

        Planilla planilla =
                findById(idPlanilla);

        planilla.cerrarPlanilla();

        return planillaRepo.save(planilla);
    }

    // =========================
    // ABRIR PLANILLA
    // =========================

    @Override
    public Planilla abrirPlanilla(Long idPlanilla) {

        Planilla planilla =
                findById(idPlanilla);

        planilla.abrirPlanilla();

        return planillaRepo.save(planilla);
    }

    // =========================
    // OBTENER PLANILLA COMPLETA
    // =========================

    @Override
    public Planilla obtenerPlanillaCompleta(Long idPlanilla) {

        return planillaRepo.findById(idPlanilla)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Planilla no encontrada"
                        )
                );
    }
}