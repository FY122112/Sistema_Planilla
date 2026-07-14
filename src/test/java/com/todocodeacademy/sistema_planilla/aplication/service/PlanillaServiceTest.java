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
                LocalDate.of(2020, 1, 1), puesto
        );
        empleado.actualizarRegimenLaboral(empleado.getRegimenLaboral(), true);
        empleado.asignarSistemaPension(onp);

        return empleado;
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

    private Planilla mockPlanillaExistente() {
        ParametroLegal rmv = new ParametroLegal(
                "RMV", "Remuneración Mínima Vital",
                new BigDecimal("1130.00"), LocalDate.of(2024, 5, 1), null
        );
        return new Planilla(7, 2026, TipoPlanilla.MENSUAL, rmv);
    }
}
