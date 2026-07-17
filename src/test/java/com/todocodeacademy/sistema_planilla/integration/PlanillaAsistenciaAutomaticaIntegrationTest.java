package com.todocodeacademy.sistema_planilla.integration;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.PlanillaServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.AsistenciaRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpleadoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.PuestoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Asistencia;
import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoCivil;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.Sexo;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoDocumento;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoPlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.MovimientoPlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Planilla;
import com.todocodeacademy.sistema_planilla.domain.model.Puesto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica que PlanillaService#aplicarAsistenciaAutomatica derive días no laborados y
 * minutos de tardanza de las marcas reales de Asistencia al generar la planilla mensual,
 * en vez de depender de que el Contador los digite a mano (ver README HU-031/HU-032/HU-033,
 * nota actualizada 2026-07-17).
 *
 * Ambos empleados de prueba se contratan el último día hábil (lunes-viernes) de un mes ya
 * pasado (enero de 1902, sin colisión con planillas reales), de forma que el rango
 * [fechaIngreso, finDeMes] contenga exactamente un día hábil: así el resultado es
 * determinista sin depender de a qué día de la semana caiga el mes.
 */
@SpringBootTest
@Transactional
class PlanillaAsistenciaAutomaticaIntegrationTest {

    @Autowired
    private PlanillaServicePort planillaService;

    @Autowired
    private EmpleadoRepositoryPort empleadoRepository;

    @Autowired
    private PuestoRepositoryPort puestoRepository;

    @Autowired
    private AsistenciaRepositoryPort asistenciaRepository;

    private static final int MES_PRUEBA = 1;
    private static final int ANIO_PRUEBA = 1902;

    private static final LocalDate ULTIMO_DIA_HABIL_DEL_MES = ultimoDiaHabilDelMes(MES_PRUEBA, ANIO_PRUEBA);

    @Test
    void faltaSinMarcarEntrada_generaDescuentoDeAusentismoAutomatico() {
        Puesto puesto = puestoRepository.save(new Puesto(
                "Puesto Ausentismo Auto " + System.nanoTime(),
                new BigDecimal("3000.00"), 8, null, null, null
        ));

        Empleado empleado = new Empleado(
                "Falta", "Automatica", "F" + System.nanoTime(),
                ULTIMO_DIA_HABIL_DEL_MES, puesto
        );
        empleado.actualizarDatosPersonales(
                TipoDocumento.DNI, LocalDate.of(1990, 1, 1),
                Sexo.M, EstadoCivil.Soltero, "Peruana"
        );
        empleado = empleadoRepository.save(empleado);

        // Sin ninguna Asistencia creada: el único día hábil del rango queda sin marcar.

        Planilla planilla = planillaService.generarPlanilla(MES_PRUEBA, ANIO_PRUEBA, TipoPlanilla.MENSUAL);
        DetallePlanilla detalle = detalleDe(planilla, empleado.getNumeroDocumento());

        assertThat(detalle.getDiasNoLaborados()).isEqualTo(1);
        assertThat(detalle.getMinutosTardanza()).isEqualTo(0);

        BigDecimal valorDiaEsperado = new BigDecimal("3000.00")
                .divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP);

        MovimientoPlanilla ausentismo = movimiento(detalle, "Ausentismo Injustificado");
        assertThat(ausentismo.getMonto()).isEqualByComparingTo(valorDiaEsperado);

        assertThat(detalle.obtenerMovimientos().stream()
                .anyMatch(m -> "Descuento por Tardanza".equals(m.getConcepto().getNombreConcepto())))
                .as("no debería haber descuento de tardanza si no hubo tardanza")
                .isFalse();
    }

    @Test
    void tardanzaAlMarcarEntrada_generaDescuentoDeTardanzaAutomatico() {
        Puesto puesto = puestoRepository.save(new Puesto(
                "Puesto Tardanza Auto " + System.nanoTime(),
                new BigDecimal("3000.00"), 8, null, null, null
        ));

        Empleado empleado = new Empleado(
                "Tardanza", "Automatica", "T" + System.nanoTime(),
                ULTIMO_DIA_HABIL_DEL_MES, puesto
        );
        empleado.actualizarDatosPersonales(
                TipoDocumento.DNI, LocalDate.of(1990, 1, 1),
                Sexo.M, EstadoCivil.Soltero, "Peruana"
        );
        empleado = empleadoRepository.save(empleado);

        // 75 minutos tarde respecto del umbral fijo (8:00) que usa AsistenciaService.
        Asistencia asistencia = Asistencia.crear(empleado, ULTIMO_DIA_HABIL_DEL_MES);
        asistencia.registrarEntrada(LocalTime.of(9, 15));
        asistencia.calcularTardanza(LocalTime.of(8, 0));
        asistenciaRepository.save(asistencia);

        Planilla planilla = planillaService.generarPlanilla(MES_PRUEBA, ANIO_PRUEBA, TipoPlanilla.MENSUAL);
        DetallePlanilla detalle = detalleDe(planilla, empleado.getNumeroDocumento());

        assertThat(detalle.getDiasNoLaborados()).isEqualTo(0);
        assertThat(detalle.getMinutosTardanza()).isEqualTo(75);

        // valorHora = (3000/30) / 8 ; valorMinuto = valorHora / 60 ; monto = valorMinuto * 75
        BigDecimal valorDia = new BigDecimal("3000.00").divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP);
        BigDecimal valorHora = valorDia.divide(BigDecimal.valueOf(8), 4, RoundingMode.HALF_UP);
        BigDecimal valorMinuto = valorHora.divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP);
        BigDecimal montoEsperado = valorMinuto.multiply(BigDecimal.valueOf(75)).setScale(2, RoundingMode.HALF_UP);

        MovimientoPlanilla tardanza = movimiento(detalle, "Descuento por Tardanza");
        assertThat(tardanza.getMonto()).isEqualByComparingTo(montoEsperado);

        assertThat(detalle.obtenerMovimientos().stream()
                .anyMatch(m -> "Ausentismo Injustificado".equals(m.getConcepto().getNombreConcepto())))
                .as("no debería haber descuento de ausentismo si sí marcó entrada")
                .isFalse();
    }

    private static LocalDate ultimoDiaHabilDelMes(int mes, int anio) {
        LocalDate dia = LocalDate.of(anio, mes, 1).withDayOfMonth(LocalDate.of(anio, mes, 1).lengthOfMonth());
        while (dia.getDayOfWeek() == DayOfWeek.SATURDAY || dia.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dia = dia.minusDays(1);
        }
        return dia;
    }

    private DetallePlanilla detalleDe(Planilla planilla, String numeroDocumento) {
        return planilla.obtenerDetalles().stream()
                .filter(d -> numeroDocumento.equals(d.getEmpleado().getNumeroDocumento()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No se encontró el detalle del empleado de prueba"));
    }

    private MovimientoPlanilla movimiento(DetallePlanilla detalle, String nombreConcepto) {
        return detalle.obtenerMovimientos().stream()
                .filter(m -> nombreConcepto.equals(m.getConcepto().getNombreConcepto()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No se encontró el movimiento '" + nombreConcepto + "'"));
    }
}
