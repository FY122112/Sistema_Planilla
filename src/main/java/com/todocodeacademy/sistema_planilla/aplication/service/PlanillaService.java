package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.PlanillaServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpleadoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.ParametroLegalRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.PlanillaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.*;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.MetodoCalculado;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoPlanilla;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        validarPeriodoParaTipo(mes, tipoPlanilla);

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
        // GENERAR DETALLES (la fórmula depende del tipo de planilla)
        // =========================

        for (Empleado empleado : empleados) {

            DetallePlanilla detalle = switch (tipoPlanilla) {
                case MENSUAL -> construirDetalleMensual(planilla, empleado, parametroAsignacion);
                case GRATIFICACION -> construirDetalleGratificacion(planilla, empleado, parametroAsignacion, mes, anio);
                case CTS -> construirDetalleCts(planilla, empleado, parametroAsignacion, mes, anio);
                case LIQUIDACION -> throw new UnsupportedOperationException(
                        "La liquidación se calcula para un empleado específico en su cese " +
                                "(CTS truncada + vacaciones truncadas + gratificación truncada), " +
                                "no como una generación masiva mensual. Aún no está implementada."
                );
            };

            planilla.agregarDetalle(detalle);
        }

        // =========================
        // GUARDAR PLANILLA
        // =========================

        return planillaRepo.save(planilla);
    }

    // =========================
    // VALIDACIÓN DE PERIODO SEGÚN LEY
    // =========================

    private void validarPeriodoParaTipo(Integer mes, TipoPlanilla tipoPlanilla) {

        if (tipoPlanilla == TipoPlanilla.GRATIFICACION && mes != 7 && mes != 12) {
            throw new IllegalArgumentException(
                    "La gratificación solo se genera en julio (1er semestre) o diciembre (2do semestre)"
            );
        }

        if (tipoPlanilla == TipoPlanilla.CTS && mes != 5 && mes != 11) {
            throw new IllegalArgumentException(
                    "La CTS solo se genera en mayo (periodo nov-abr) o noviembre (periodo may-oct)"
            );
        }
    }

    // =========================
    // CÁLCULO: PLANILLA MENSUAL
    // =========================

    private DetallePlanilla construirDetalleMensual(
            Planilla planilla,
            Empleado empleado,
            ParametroLegal parametroAsignacion
    ) {

        BigDecimal sueldoBase = sueldoBaseDe(empleado);

        DetallePlanilla detalle = new DetallePlanilla(planilla, empleado, sueldoBase);

        if (Boolean.TRUE.equals(empleado.isTieneHijosCalificados()) && parametroAsignacion != null) {
            detalle.actualizarAsignacionFamiliar(parametroAsignacion.getValor());
        }

        aplicarDescuentoPension(detalle, empleado, sueldoBase);

        detalle.actualizarRemuneracionComputable(detalle.getSueldoBruto());
        detalle.recalcularTotales();

        return detalle;
    }

    // =========================
    // CÁLCULO: GRATIFICACIÓN (julio / diciembre)
    // =========================
    //
    // Monto = (remuneración computable / 6) x meses completos laborados en el semestre.
    // Se suma la Bonificación Extraordinaria (9%, reemplaza el aporte a EsSalud que
    // hubiera pagado el empleador). Por ley, la gratificación y su bonificación
    // extraordinaria están inafectas a los descuentos de AFP/ONP.

    private static final BigDecimal PORCENTAJE_BONIFICACION_EXTRAORDINARIA = new BigDecimal("0.09");

    private DetallePlanilla construirDetalleGratificacion(
            Planilla planilla,
            Empleado empleado,
            ParametroLegal parametroAsignacion,
            Integer mes,
            Integer anio
    ) {

        BigDecimal sueldoBase = sueldoBaseDe(empleado);

        LocalDate inicioSemestre = mes == 7 ? LocalDate.of(anio, 1, 1) : LocalDate.of(anio, 7, 1);
        LocalDate finSemestre = mes == 7 ? LocalDate.of(anio, 6, 30) : LocalDate.of(anio, 12, 31);

        int mesesComputables = mesesComputables(empleado.getFechaIngreso(), inicioSemestre, finSemestre);

        BigDecimal asignacionFamiliar = asignacionFamiliarDe(empleado, parametroAsignacion);
        BigDecimal remuneracionComputable = sueldoBase.add(asignacionFamiliar);

        BigDecimal montoGratificacion = remuneracionComputable
                .divide(new BigDecimal("6"), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(mesesComputables));

        // El monto de la gratificación ocupa el campo "sueldoBase" del detalle: para este
        // tipo de planilla no representa el sueldo mensual del puesto, sino el ingreso
        // bruto de este concepto (igual que en CTS).
        DetallePlanilla detalle = new DetallePlanilla(planilla, empleado, montoGratificacion);

        BigDecimal bonificacionExtraordinaria = montoGratificacion
                .multiply(PORCENTAJE_BONIFICACION_EXTRAORDINARIA)
                .setScale(2, RoundingMode.HALF_UP);

        // esRemunerativo=true es lo que hace que MovimientoPlanilla.esIngreso() la sume a
        // totalIngresosAdicionales en vez de tratarla como descuento (ver
        // MovimientoPlanilla.esIngreso(), que se basa en este flag y no en TipoConcepto).
        ConceptoPago conceptoBonificacion = new ConceptoPago(
                "BONIF_EXTRAORD",
                "Bonificación Extraordinaria (9%)",
                TipoConcepto.INGRESO,
                MetodoCalculado.PORCENTAJE,
                true
        );

        detalle.agregarMovimiento(
                new MovimientoPlanilla(detalle, conceptoBonificacion, bonificacionExtraordinaria)
        );

        detalle.actualizarRemuneracionComputable(remuneracionComputable);
        detalle.recalcularTotales();

        return detalle;
    }

    // =========================
    // CÁLCULO: CTS (mayo / noviembre)
    // =========================
    //
    // Monto = (remuneración computable / 12) x meses completos laborados en el periodo.
    // La remuneración computable incluye 1/6 de la gratificación; como el sistema todavía
    // no guarda el historial de gratificaciones realmente pagadas, se aproxima como
    // sueldoBase/6 (equivalente a un semestre completo). Por ley, la CTS está inafecta
    // a los descuentos de AFP/ONP.

    private DetallePlanilla construirDetalleCts(
            Planilla planilla,
            Empleado empleado,
            ParametroLegal parametroAsignacion,
            Integer mes,
            Integer anio
    ) {

        BigDecimal sueldoBase = sueldoBaseDe(empleado);

        LocalDate inicioPeriodo = mes == 5 ? LocalDate.of(anio - 1, 11, 1) : LocalDate.of(anio, 5, 1);
        LocalDate finPeriodo = mes == 5 ? LocalDate.of(anio, 4, 30) : LocalDate.of(anio, 10, 31);

        int mesesComputables = mesesComputables(empleado.getFechaIngreso(), inicioPeriodo, finPeriodo);

        BigDecimal asignacionFamiliar = asignacionFamiliarDe(empleado, parametroAsignacion);
        BigDecimal sextoGratificacion = sueldoBase.divide(new BigDecimal("6"), 2, RoundingMode.HALF_UP);

        BigDecimal remuneracionComputable = sueldoBase.add(asignacionFamiliar).add(sextoGratificacion);

        BigDecimal montoCts = remuneracionComputable
                .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(mesesComputables));

        DetallePlanilla detalle = new DetallePlanilla(planilla, empleado, montoCts);

        detalle.actualizarRemuneracionComputable(remuneracionComputable);
        detalle.recalcularTotales();

        return detalle;
    }

    // =========================
    // HELPERS COMPARTIDOS
    // =========================

    private BigDecimal sueldoBaseDe(Empleado empleado) {
        return empleado.getPuesto() != null
                ? empleado.getPuesto().getSalarioBase()
                : BigDecimal.ZERO;
    }

    private BigDecimal asignacionFamiliarDe(Empleado empleado, ParametroLegal parametroAsignacion) {
        return Boolean.TRUE.equals(empleado.isTieneHijosCalificados()) && parametroAsignacion != null
                ? parametroAsignacion.getValor()
                : BigDecimal.ZERO;
    }

    private void aplicarDescuentoPension(DetallePlanilla detalle, Empleado empleado, BigDecimal baseCalculo) {

        SistemaPension sistema = empleado.getSistemaPension();

        if (sistema == null) {
            return;
        }

        BigDecimal descuentoPension = sistema.calcularDescuento(baseCalculo);

        ConceptoPago conceptoPension = new ConceptoPago(
                "AFP_ONP",
                "Descuento Pensionario",
                TipoConcepto.DESCUENTO,
                MetodoCalculado.PORCENTAJE,
                false
        );

        detalle.agregarMovimiento(
                new MovimientoPlanilla(detalle, conceptoPension, descuentoPension)
        );
    }

    // Cuenta los meses calendario completos entre la fecha de ingreso (o el inicio del
    // periodo, lo que sea posterior) y el fin del periodo, con tope de 6 meses.
    private int mesesComputables(LocalDate fechaIngreso, LocalDate inicioPeriodo, LocalDate finPeriodo) {

        if (fechaIngreso == null || fechaIngreso.isAfter(finPeriodo)) {
            return 0;
        }

        LocalDate desde = fechaIngreso.isAfter(inicioPeriodo) ? fechaIngreso : inicioPeriodo;

        int meses = (finPeriodo.getYear() - desde.getYear()) * 12
                + (finPeriodo.getMonthValue() - desde.getMonthValue())
                + 1;

        return Math.max(0, Math.min(meses, 6));
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