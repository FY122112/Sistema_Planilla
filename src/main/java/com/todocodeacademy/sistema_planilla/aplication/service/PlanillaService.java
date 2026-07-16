package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.command.DetalleMensualCommand;
import com.todocodeacademy.sistema_planilla.aplication.command.ResumenMensual;
import com.todocodeacademy.sistema_planilla.aplication.ports.input.PlanillaServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.AuditoriaCambioRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.ConceptoPagoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpleadoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.ParametroLegalRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.PlanillaExcelExporterPort;
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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlanillaService implements PlanillaServicePort {

    private final PlanillaRepositoryPort planillaRepo;
    private final EmpleadoRepositoryPort empleadoRepo;
    private final ParametroLegalRepositoryPort parametroRepo;
    private final ConceptoPagoRepositoryPort conceptoPagoRepo;
    private final PlanillaExcelExporterPort excelExporter;
    private final AuditoriaCambioRepositoryPort auditoriaRepo;

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
        // OBTENER ESSALUD Y VIDA LEY (aportes del empleador, opcionales)
        // =========================

        ParametroLegal parametroEssalud =
                parametroRepo
                        .findTopByCodigoOrderByFechaInicioVigenciaDesc("ESSALUD")
                        .orElse(null);

        ParametroLegal parametroVidaLey =
                parametroRepo
                        .findTopByCodigoOrderByFechaInicioVigenciaDesc("VIDA_LEY")
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
                case MENSUAL -> construirDetalleMensual(planilla, empleado, parametroAsignacion, parametroEssalud, parametroVidaLey);
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
            ParametroLegal parametroAsignacion,
            ParametroLegal parametroEssalud,
            ParametroLegal parametroVidaLey
    ) {

        BigDecimal sueldoBase = sueldoBaseDe(empleado);

        DetallePlanilla detalle = new DetallePlanilla(planilla, empleado, sueldoBase);

        if (Boolean.TRUE.equals(empleado.isTieneHijosCalificados()) && parametroAsignacion != null) {
            detalle.actualizarAsignacionFamiliar(parametroAsignacion.getValor());
        }

        // Al generar la planilla las 7 variables mensuales del detalle están todas en
        // cero (ver DetallePlanilla constructor), así que este primer cálculo no agrega
        // ningún movimiento derivado de asistencia; el Contador los agrega luego editando
        // el detalle (ver PlanillaService#previsualizarDetalleMensual / #actualizarDetalleMensual).
        aplicarVariablesMensuales(detalle, empleado, parametroEssalud, parametroVidaLey);

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

        ConceptoPago conceptoBonificacion = conceptoIngreso(
                "BONIF_EXTRAORD", "Bonificación Extraordinaria (9%)", true
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

    // =========================
    // VARIABLES MENSUALES (HU-031 a HU-045)
    // =========================
    //
    // Traduce las 7 variables de entrada manual del detalle (días no laborados,
    // minutos de tardanza, horas extra 25%/35%, días de vacaciones gozadas,
    // bonificación de eficiencia, comisión comercial) en los movimientos monetarios
    // correspondientes. Se llama tanto al generar la planilla (con las 7 variables en
    // cero) como al editar un detalle ya existente (después de reiniciarMovimientos()).

    private static final BigDecimal FACTOR_HORA_EXTRA_25 = new BigDecimal("1.25");
    private static final BigDecimal FACTOR_HORA_EXTRA_35 = new BigDecimal("1.35");
    private static final BigDecimal DIAS_MES = new BigDecimal("30");
    private static final BigDecimal MINUTOS_HORA = new BigDecimal("60");
    private static final int HORAS_JORNADA_POR_DEFECTO = 8;

    private void aplicarVariablesMensuales(
            DetallePlanilla detalle,
            Empleado empleado,
            ParametroLegal parametroEssalud,
            ParametroLegal parametroVidaLey
    ) {

        BigDecimal sueldoBase = detalle.getSueldoBase();

        int horasJornada = empleado.getPuesto() != null
                && empleado.getPuesto().getJornadaLaboralHoras() != null
                && empleado.getPuesto().getJornadaLaboralHoras() > 0
                ? empleado.getPuesto().getJornadaLaboralHoras()
                : HORAS_JORNADA_POR_DEFECTO;

        BigDecimal valorDia = sueldoBase.divide(DIAS_MES, 2, RoundingMode.HALF_UP);
        BigDecimal valorHora = valorDia.divide(BigDecimal.valueOf(horasJornada), 4, RoundingMode.HALF_UP);

        // Ausentismo / días no laborados (HU-031 / HU-032)
        if (detalle.getDiasNoLaborados() != null && detalle.getDiasNoLaborados() > 0) {

            BigDecimal monto = valorDia
                    .multiply(BigDecimal.valueOf(detalle.getDiasNoLaborados()))
                    .setScale(2, RoundingMode.HALF_UP);

            detalle.agregarMovimiento(new MovimientoPlanilla(
                    detalle,
                    conceptoDescuento("AUSENTISMO", "Ausentismo Injustificado"),
                    monto
            ));
        }

        // Tardanzas (HU-033)
        if (detalle.getMinutosTardanza() != null && detalle.getMinutosTardanza() > 0) {

            BigDecimal valorMinuto = valorHora.divide(MINUTOS_HORA, 4, RoundingMode.HALF_UP);

            BigDecimal monto = valorMinuto
                    .multiply(BigDecimal.valueOf(detalle.getMinutosTardanza()))
                    .setScale(2, RoundingMode.HALF_UP);

            detalle.agregarMovimiento(new MovimientoPlanilla(
                    detalle,
                    conceptoDescuento("TARDANZA", "Descuento por Tardanza"),
                    monto
            ));
        }

        // Horas extra 25% / 35% (HU-034 / HU-035)
        BigDecimal montoHoraExtra25 = BigDecimal.ZERO;
        if (detalle.getHorasExtras25() != null && detalle.getHorasExtras25().compareTo(BigDecimal.ZERO) > 0) {

            montoHoraExtra25 = valorHora
                    .multiply(FACTOR_HORA_EXTRA_25)
                    .multiply(detalle.getHorasExtras25())
                    .setScale(2, RoundingMode.HALF_UP);

            detalle.agregarMovimiento(new MovimientoPlanilla(
                    detalle,
                    conceptoIngreso("HORA_EXTRA_25", "Horas Extra 25%", true),
                    montoHoraExtra25
            ));
        }

        BigDecimal montoHoraExtra35 = BigDecimal.ZERO;
        if (detalle.getHorasExtras35() != null && detalle.getHorasExtras35().compareTo(BigDecimal.ZERO) > 0) {

            montoHoraExtra35 = valorHora
                    .multiply(FACTOR_HORA_EXTRA_35)
                    .multiply(detalle.getHorasExtras35())
                    .setScale(2, RoundingMode.HALF_UP);

            detalle.agregarMovimiento(new MovimientoPlanilla(
                    detalle,
                    conceptoIngreso("HORA_EXTRA_35", "Horas Extra 35%", true),
                    montoHoraExtra35
            ));
        }

        // Vacaciones gozadas (HU-036) — ingreso separado, sin descuento asociado
        BigDecimal montoVacaciones = BigDecimal.ZERO;
        if (detalle.getDiasVacacionesGozadas() != null && detalle.getDiasVacacionesGozadas() > 0) {

            montoVacaciones = valorDia
                    .multiply(BigDecimal.valueOf(detalle.getDiasVacacionesGozadas()))
                    .setScale(2, RoundingMode.HALF_UP);

            detalle.agregarMovimiento(new MovimientoPlanilla(
                    detalle,
                    conceptoIngreso("VACACIONES_GOZADAS", "Vacaciones Gozadas", true),
                    montoVacaciones
            ));
        }

        // Comisión comercial (HU-043) — afecta a las bases de AFP/EsSalud
        BigDecimal comisionComercial = detalle.getComisionComercial() != null
                ? detalle.getComisionComercial()
                : BigDecimal.ZERO;

        if (comisionComercial.compareTo(BigDecimal.ZERO) > 0) {
            detalle.agregarMovimiento(new MovimientoPlanilla(
                    detalle,
                    conceptoIngreso("COMISION_COMERCIAL", "Comisión Comercial", true),
                    comisionComercial
            ));
        }

        // Base computable para AFP/ONP, EsSalud y Vida Ley: todo lo remunerativo
        // calculado hasta este punto (la bonificación de eficiencia, no remunerativa,
        // se agrega después y queda fuera de esta base a propósito, ver HU-042).
        BigDecimal remuneracionComputable = sueldoBase
                .add(detalle.getAsignacionFamiliar())
                .add(montoHoraExtra25)
                .add(montoHoraExtra35)
                .add(montoVacaciones)
                .add(comisionComercial);

        // AFP / ONP desglosado en aporte + comisión (HU-038 / HU-039)
        SistemaPension sistema = empleado.getSistemaPension();
        if (sistema != null) {

            BigDecimal aporte = remuneracionComputable
                    .multiply(sistema.getPorcentajeAporte())
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal comisionAfp = remuneracionComputable
                    .multiply(sistema.getPorcentajeComision())
                    .setScale(2, RoundingMode.HALF_UP);

            detalle.agregarMovimiento(new MovimientoPlanilla(
                    detalle,
                    conceptoDescuento("AFP_APORTE", "Aporte al Sistema de Pensiones"),
                    aporte
            ));

            detalle.agregarMovimiento(new MovimientoPlanilla(
                    detalle,
                    conceptoDescuento("AFP_COMISION", "Comisión AFP"),
                    comisionAfp
            ));
        }

        // EsSalud — aporte del empleador, no reduce el neto (HU-040)
        if (parametroEssalud != null) {

            BigDecimal montoEssalud = remuneracionComputable
                    .multiply(parametroEssalud.getValor())
                    .setScale(2, RoundingMode.HALF_UP);

            detalle.agregarMovimiento(new MovimientoPlanilla(
                    detalle,
                    conceptoAporteEmpleador("ESSALUD", "Aporte EsSalud"),
                    montoEssalud
            ));
        }

        // Vida Ley — aporte del empleador, monto fijo mensual (HU-041)
        if (parametroVidaLey != null) {
            detalle.agregarMovimiento(new MovimientoPlanilla(
                    detalle,
                    conceptoAporteEmpleador("VIDA_LEY", "Seguro Vida Ley"),
                    parametroVidaLey.getValor()
            ));
        }

        // Bonificación de eficiencia (HU-042) — ingreso no remunerativo, se agrega al
        // final para que quede fuera de la base de AFP/EsSalud/Vida Ley calculada arriba.
        BigDecimal bonificacionEficiencia = detalle.getBonificacionEficiencia() != null
                ? detalle.getBonificacionEficiencia()
                : BigDecimal.ZERO;

        if (bonificacionEficiencia.compareTo(BigDecimal.ZERO) > 0) {
            detalle.agregarMovimiento(new MovimientoPlanilla(
                    detalle,
                    conceptoIngreso("BONIFICACION_EFICIENCIA", "Bonificación de Eficiencia", false),
                    bonificacionEficiencia
            ));
        }

        detalle.actualizarRemuneracionComputable(remuneracionComputable);
        detalle.recalcularTotales();
    }

    private ConceptoPago conceptoDescuento(String codigo, String nombre) {
        return resolverConcepto(codigo, nombre, TipoConcepto.DESCUENTO, false);
    }

    private ConceptoPago conceptoIngreso(String codigo, String nombre, boolean esRemunerativo) {
        return resolverConcepto(codigo, nombre, TipoConcepto.INGRESO, esRemunerativo);
    }

    private ConceptoPago conceptoAporteEmpleador(String codigo, String nombre) {
        return resolverConcepto(codigo, nombre, TipoConcepto.APORTE_EMPLEADOR, false);
    }

    // Los conceptos de pago son un catálogo persistido (FK obligatoria en
    // MovimientoPlanillaEntity.concepto): no se puede construir un ConceptoPago
    // transitorio y usarlo directo en un movimiento porque no tiene id. Se busca por
    // nombre y, si no existe todavía, se crea una única vez en el catálogo.
    private ConceptoPago resolverConcepto(
            String codigo,
            String nombre,
            TipoConcepto tipoConcepto,
            boolean esRemunerativo
    ) {
        return conceptoPagoRepo.findByNombreConcepto(nombre)
                .orElseGet(() -> conceptoPagoRepo.save(
                        new ConceptoPago(codigo, nombre, tipoConcepto, MetodoCalculado.PORCENTAJE, esRemunerativo)
                ));
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

    // =========================
    // DETALLE MENSUAL: PREVIEW Y ACTUALIZACIÓN (HU-031 a HU-045)
    // =========================

    @Override
    public DetallePlanilla previsualizarDetalleMensual(
            Long idPlanilla,
            Long idDetalle,
            DetalleMensualCommand command
    ) {

        Planilla planilla = findById(idPlanilla);

        DetallePlanilla detalle = obtenerDetalleDePlanilla(planilla, idDetalle);

        aplicarComandoADetalle(detalle, command);

        return detalle;
    }

    @Override
    public DetallePlanilla actualizarDetalleMensual(
            Long idPlanilla,
            Long idDetalle,
            DetalleMensualCommand command,
            String usuario
    ) {

        Planilla planilla = findById(idPlanilla);

        if (planilla.estaCerrada()) {
            throw new IllegalStateException(
                    "No se puede modificar el detalle de una planilla cerrada"
            );
        }

        DetallePlanilla detalle = obtenerDetalleDePlanilla(planilla, idDetalle);

        BigDecimal montoAnterior = detalle.getSueldoNeto();

        aplicarComandoADetalle(detalle, command);

        planillaRepo.save(planilla);

        // HU-012: bitácora de auditoría — se registra siempre que el neto realmente
        // cambie, para no llenar la tabla con "ediciones" que reenviaron los mismos valores.
        if (montoAnterior == null || montoAnterior.compareTo(detalle.getSueldoNeto()) != 0) {
            auditoriaRepo.save(new AuditoriaCambio(
                    usuario, idPlanilla, idDetalle, montoAnterior, detalle.getSueldoNeto()
            ));
        }

        return detalle;
    }

    @Override
    public List<AuditoriaCambio> obtenerAuditoria(Long idPlanilla) {
        return auditoriaRepo.findByIdPlanilla(idPlanilla);
    }

    private DetallePlanilla obtenerDetalleDePlanilla(Planilla planilla, Long idDetalle) {

        return planilla.obtenerDetalles().stream()
                .filter(d -> idDetalle.equals(d.getIdDetalle()))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "El detalle no pertenece a esta planilla"
                        )
                );
    }

    private void aplicarComandoADetalle(DetallePlanilla detalle, DetalleMensualCommand command) {

        detalle.actualizarVariablesMensuales(
                command.diasNoLaborados(),
                command.minutosTardanza(),
                command.horasExtras25(),
                command.horasExtras35(),
                command.diasVacacionesGozadas(),
                command.vacacionesFechaInicio(),
                command.vacacionesFechaFin(),
                command.bonificacionEficiencia(),
                command.comisionComercial()
        );

        detalle.reiniciarMovimientos();

        ParametroLegal parametroEssalud =
                parametroRepo
                        .findTopByCodigoOrderByFechaInicioVigenciaDesc("ESSALUD")
                        .orElse(null);

        ParametroLegal parametroVidaLey =
                parametroRepo
                        .findTopByCodigoOrderByFechaInicioVigenciaDesc("VIDA_LEY")
                        .orElse(null);

        aplicarVariablesMensuales(detalle, detalle.getEmpleado(), parametroEssalud, parametroVidaLey);
    }

    @Override
    public byte[] exportarExcel(Long idPlanilla) {
        Planilla planilla = findById(idPlanilla);
        return excelExporter.export(planilla);
    }

    // =========================
    // RESUMEN MENSUAL (HU-015)
    // =========================
    //
    // Suma el neto de todas las planillas (mensual, gratificación, CTS...) generadas en
    // cada periodo: la HU pide "el histórico de gastos salariales", no solo el sueldo
    // regular, así que un mes con gratificación debe reflejar ese gasto adicional.

    @Override
    public List<ResumenMensual> resumenMensual(int ultimosMeses) {

        Map<String, ResumenMensual> porPeriodo = new LinkedHashMap<>();

        for (Planilla planilla : planillaRepo.findAll()) {

            String clave = planilla.getAnio() + "-" + planilla.getMes();

            ResumenMensual acumulado = porPeriodo.get(clave);
            BigDecimal totalPrevio = acumulado != null ? acumulado.totalNeto() : BigDecimal.ZERO;

            porPeriodo.put(clave, new ResumenMensual(
                    planilla.getMes(),
                    planilla.getAnio(),
                    totalPrevio.add(planilla.calcularTotalNeto())
            ));
        }

        List<ResumenMensual> ordenado = porPeriodo.values().stream()
                .sorted(Comparator.comparing(ResumenMensual::anio).thenComparing(ResumenMensual::mes))
                .toList();

        return ultimosMeses > 0 && ordenado.size() > ultimosMeses
                ? ordenado.subList(ordenado.size() - ultimosMeses, ordenado.size())
                : ordenado;
    }
}