package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpleadoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.ParametroLegalRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.PlanillaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoPlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.ParametroLegal;
import com.todocodeacademy.sistema_planilla.domain.model.Planilla;
import com.todocodeacademy.sistema_planilla.domain.model.Puesto;
import com.todocodeacademy.sistema_planilla.domain.model.SistemaPension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Prueba, con mocks, el mismo flujo que verifiqué manualmente contra la app real:
 * generar la planilla mensual calculando asignación familiar y descuento de pensión.
 * ParametroLegalAdapter estaba stubbeado para devolver siempre Optional.empty(), así
 * que esto reproduce exactamente el "No existe parámetro RMV" que vi en la prueba en vivo.
 */
@ExtendWith(MockitoExtension.class)
class PlanillaServiceTest {

    @Mock
    private PlanillaRepositoryPort planillaRepo;

    @Mock
    private EmpleadoRepositoryPort empleadoRepo;

    @Mock
    private ParametroLegalRepositoryPort parametroRepo;

    @InjectMocks
    private PlanillaService planillaService;

    private Empleado empleadoConHijosYPension() {
        return empleadoConHijosYPension(LocalDate.of(2020, 1, 1));
    }

    private Empleado empleadoConHijosYPension(LocalDate fechaIngreso) {
        Puesto puesto = new Puesto(
                "Operario de Confección",
                new BigDecimal("3000.00"),
                8, null, null, null
        );

        SistemaPension onp = new SistemaPension(
                "ONP", "PUBLICO",
                new BigDecimal("0.13"), BigDecimal.ZERO
        );

        Empleado empleado = new Empleado(
                "Carlos", "Ramirez", "12345678",
                fechaIngreso, puesto
        );
        empleado.actualizarRegimenLaboral(empleado.getRegimenLaboral(), true);
        empleado.asignarSistemaPension(onp);

        return empleado;
    }

    // Sin hijos calificados: deja la asignación familiar en 0 para que los montos de
    // Gratificación/CTS den cifras exactas y no queden enredados con el redondeo de
    // sumarle 113.00 antes de dividir entre 6/12.
    private Empleado empleadoSinHijos(LocalDate fechaIngreso) {
        Puesto puesto = new Puesto(
                "Operario de Confección",
                new BigDecimal("3000.00"),
                8, null, null, null
        );

        SistemaPension onp = new SistemaPension(
                "ONP", "PUBLICO",
                new BigDecimal("0.13"), BigDecimal.ZERO
        );

        Empleado empleado = new Empleado(
                "Luis", "Quispe", "87654321",
                fechaIngreso, puesto
        );
        empleado.asignarSistemaPension(onp);

        return empleado;
    }

    private void stubParametrosBase() {
        when(parametroRepo.findTopByCodigoOrderByFechaInicioVigenciaDesc("RMV"))
                .thenReturn(Optional.of(new ParametroLegal(
                        "RMV", "Remuneración Mínima Vital",
                        new BigDecimal("1130.00"), LocalDate.of(2024, 5, 1), null
                )));
        when(parametroRepo.findTopByCodigoOrderByFechaInicioVigenciaDesc("ASIGNACION_FAMILIAR"))
                .thenReturn(Optional.of(new ParametroLegal(
                        "ASIGNACION_FAMILIAR", "Asignación Familiar",
                        new BigDecimal("113.00"), LocalDate.of(2024, 5, 1), null
                )));
        when(planillaRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void generarPlanilla_debeCalcularAsignacionFamiliarYDescuentoPension() {
        when(planillaRepo.findByMesAndAnioAndTipoPlanilla(7, 2026, TipoPlanilla.MENSUAL))
                .thenReturn(Optional.empty());
        when(empleadoRepo.findByEstado(true))
                .thenReturn(List.of(empleadoConHijosYPension()));
        when(parametroRepo.findTopByCodigoOrderByFechaInicioVigenciaDesc("RMV"))
                .thenReturn(Optional.of(new ParametroLegal(
                        "RMV", "Remuneración Mínima Vital",
                        new BigDecimal("1130.00"), LocalDate.of(2024, 5, 1), null
                )));
        when(parametroRepo.findTopByCodigoOrderByFechaInicioVigenciaDesc("ASIGNACION_FAMILIAR"))
                .thenReturn(Optional.of(new ParametroLegal(
                        "ASIGNACION_FAMILIAR", "Asignación Familiar",
                        new BigDecimal("113.00"), LocalDate.of(2024, 5, 1), null
                )));
        when(planillaRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Planilla planilla = planillaService.generarPlanilla(7, 2026, TipoPlanilla.MENSUAL);

        assertThat(planilla.cantidadDetalles()).isEqualTo(1);

        var detalle = planilla.obtenerDetalles().get(0);
        assertThat(detalle.getSueldoBase()).isEqualByComparingTo("3000.00");
        assertThat(detalle.getAsignacionFamiliar()).isEqualByComparingTo("113.00");
        assertThat(detalle.getSueldoBruto()).isEqualByComparingTo("3113.00");
        assertThat(detalle.getTotalDescuento()).isEqualByComparingTo("390.00");
        assertThat(detalle.getSueldoNeto()).isEqualByComparingTo("2723.00");
    }

    @Test
    void generarPlanilla_debeLanzarExcepcion_cuandoLaPlanillaYaExiste() {
        when(planillaRepo.findByMesAndAnioAndTipoPlanilla(7, 2026, TipoPlanilla.MENSUAL))
                .thenReturn(Optional.of(mockPlanillaExistente()));

        assertThatThrownBy(() -> planillaService.generarPlanilla(7, 2026, TipoPlanilla.MENSUAL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ya existe");
    }

    @Test
    void generarPlanilla_debeLanzarExcepcion_cuandoNoHayEmpleadosActivos() {
        when(planillaRepo.findByMesAndAnioAndTipoPlanilla(7, 2026, TipoPlanilla.MENSUAL))
                .thenReturn(Optional.empty());
        when(empleadoRepo.findByEstado(true)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> planillaService.generarPlanilla(7, 2026, TipoPlanilla.MENSUAL))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existen empleados activos");
    }

    @Test
    void generarPlanilla_debeLanzarExcepcion_cuandoNoExisteParametroRmv() {
        when(planillaRepo.findByMesAndAnioAndTipoPlanilla(7, 2026, TipoPlanilla.MENSUAL))
                .thenReturn(Optional.empty());
        when(empleadoRepo.findByEstado(true))
                .thenReturn(List.of(empleadoConHijosYPension()));
        when(parametroRepo.findTopByCodigoOrderByFechaInicioVigenciaDesc(eq("RMV")))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> planillaService.generarPlanilla(7, 2026, TipoPlanilla.MENSUAL))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("RMV");
    }

    // =========================
    // GRATIFICACIÓN
    // =========================

    @Test
    void generarPlanilla_debeCalcularGratificacionCompleta_paraEmpleadoConSemestreCompleto() {
        when(planillaRepo.findByMesAndAnioAndTipoPlanilla(7, 2026, TipoPlanilla.GRATIFICACION))
                .thenReturn(Optional.empty());
        when(empleadoRepo.findByEstado(true))
                .thenReturn(List.of(empleadoSinHijos(LocalDate.of(2020, 1, 1))));
        stubParametrosBase();

        Planilla planilla = planillaService.generarPlanilla(7, 2026, TipoPlanilla.GRATIFICACION);

        var detalle = planilla.obtenerDetalles().get(0);
        // sueldoBase del detalle = monto de gratificación (3000/6 * 6 meses) = 3000.00
        assertThat(detalle.getSueldoBase()).isEqualByComparingTo("3000.00");
        // bonificación extraordinaria 9% = 270.00, va como ingreso adicional (no como descuento)
        assertThat(detalle.getTotalIngresosAdicionales()).isEqualByComparingTo("270.00");
        assertThat(detalle.getSueldoBruto()).isEqualByComparingTo("3270.00"); // 3000 gratificación + 270 bonificación
        // inafecta a AFP/ONP aunque el empleado tenga sistema de pensión asignado
        assertThat(detalle.getTotalDescuento()).isEqualByComparingTo("0.00");
        assertThat(detalle.getSueldoNeto()).isEqualByComparingTo("3270.00");
    }

    @Test
    void generarPlanilla_debeProratearGratificacion_segunMesesTrabajadosEnElSemestre() {
        // ingresa el 15 de marzo de 2026: dentro del semestre ene-jun computan marzo..junio = 4 meses
        when(planillaRepo.findByMesAndAnioAndTipoPlanilla(7, 2026, TipoPlanilla.GRATIFICACION))
                .thenReturn(Optional.empty());
        when(empleadoRepo.findByEstado(true))
                .thenReturn(List.of(empleadoSinHijos(LocalDate.of(2026, 3, 15))));
        stubParametrosBase();

        Planilla planilla = planillaService.generarPlanilla(7, 2026, TipoPlanilla.GRATIFICACION);

        var detalle = planilla.obtenerDetalles().get(0);
        // (3000/6) * 4 meses = 2000.00
        assertThat(detalle.getSueldoBase()).isEqualByComparingTo("2000.00");
    }

    @Test
    void generarPlanilla_debeLanzarExcepcion_cuandoGratificacionNoEsEnJulioODiciembre() {
        assertThatThrownBy(() -> planillaService.generarPlanilla(3, 2026, TipoPlanilla.GRATIFICACION))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("julio");
    }

    // =========================
    // CTS
    // =========================

    @Test
    void generarPlanilla_debeCalcularCts_paraEmpleadoConPeriodoCompleto() {
        when(planillaRepo.findByMesAndAnioAndTipoPlanilla(11, 2026, TipoPlanilla.CTS))
                .thenReturn(Optional.empty());
        when(empleadoRepo.findByEstado(true))
                .thenReturn(List.of(empleadoSinHijos(LocalDate.of(2020, 1, 1))));
        stubParametrosBase();

        Planilla planilla = planillaService.generarPlanilla(11, 2026, TipoPlanilla.CTS);

        var detalle = planilla.obtenerDetalles().get(0);
        // remuneración computable = 3000 sueldo + 500.00 (sexto de gratificación, 3000/6) = 3500.00
        // monto CTS = (3500/12 redondeado a 291.67) * 6 meses = 1750.02
        assertThat(detalle.getSueldoBase()).isEqualByComparingTo("1750.02");
        // inafecta a AFP/ONP
        assertThat(detalle.getTotalDescuento()).isEqualByComparingTo("0.00");
    }

    @Test
    void generarPlanilla_debeLanzarExcepcion_cuandoCtsNoEsEnMayoONoviembre() {
        assertThatThrownBy(() -> planillaService.generarPlanilla(1, 2026, TipoPlanilla.CTS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("mayo");
    }

    // =========================
    // LIQUIDACIÓN (todavía no soportada en la generación masiva)
    // =========================

    @Test
    void generarPlanilla_debeLanzarExcepcion_cuandoTipoEsLiquidacion() {
        // Se lanza dentro del bucle, antes de llegar a planillaRepo.save(...), así que solo
        // se estuban RMV/ASIGNACION_FAMILIAR (necesarios antes del bucle) y no save().
        when(planillaRepo.findByMesAndAnioAndTipoPlanilla(7, 2026, TipoPlanilla.LIQUIDACION))
                .thenReturn(Optional.empty());
        when(empleadoRepo.findByEstado(true))
                .thenReturn(List.of(empleadoConHijosYPension()));
        when(parametroRepo.findTopByCodigoOrderByFechaInicioVigenciaDesc("RMV"))
                .thenReturn(Optional.of(new ParametroLegal(
                        "RMV", "Remuneración Mínima Vital",
                        new BigDecimal("1130.00"), LocalDate.of(2024, 5, 1), null
                )));

        assertThatThrownBy(() -> planillaService.generarPlanilla(7, 2026, TipoPlanilla.LIQUIDACION))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    private Planilla mockPlanillaExistente() {
        ParametroLegal rmv = new ParametroLegal(
                "RMV", "Remuneración Mínima Vital",
                new BigDecimal("1130.00"), LocalDate.of(2024, 5, 1), null
        );
        return new Planilla(7, 2026, TipoPlanilla.MENSUAL, rmv);
    }
}
