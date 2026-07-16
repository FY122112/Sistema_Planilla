package com.todocodeacademy.sistema_planilla.integration;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.PlanillaServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpleadoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.ParametroLegalRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.PuestoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.SistemaPensionRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoCivil;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.RegimenLaboral;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.Sexo;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoDocumento;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoPlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.ParametroLegal;
import com.todocodeacademy.sistema_planilla.domain.model.Planilla;
import com.todocodeacademy.sistema_planilla.domain.model.Puesto;
import com.todocodeacademy.sistema_planilla.domain.model.SistemaPension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba de integración: levanta el contexto real de Spring y habla con la base de
 * datos real (misma configuración de application.properties), pasando por las capas
 * que un test unitario con mocks se salta por completo: JpaRepository, RepositoryAdapter
 * y los EntMapper entidad<->dominio.
 *
 * Reproduce exactamente el flujo que verifiqué a mano el 2026-07-14 y que encontró tres
 * bugs reales:
 *  1) ParametroLegalAdapter devolvía siempre Optional.empty() en vez de delegar al repo JPA.
 *  2) DetallePlanillaEntMapper reconstruye el detalle con planilla=null (para evitar el ciclo
 *     planilla<->detalle) pero DetallePlanilla.reconstruir exigía planilla no nula.
 *  3) PlanillaEntMapper reataba los detalles con agregarDetalle(), que bloquea la operación
 *     si la planilla está cerrada, impidiendo releer una planilla ya cerrada.
 *
 * @Transactional revierte todo lo escrito en esta prueba al terminar: no queda ningún dato
 * de prueba en la base real.
 */
@SpringBootTest
@Transactional
class PlanillaGenerationIntegrationTest {

    @Autowired
    private PlanillaServicePort planillaService;

    @Autowired
    private EmpleadoRepositoryPort empleadoRepository;

    @Autowired
    private ParametroLegalRepositoryPort parametroLegalRepository;

    @Autowired
    private PuestoRepositoryPort puestoRepository;

    @Autowired
    private SistemaPensionRepositoryPort sistemaPensionRepository;

    // Periodo con muy baja probabilidad de colisionar con una planilla real ya generada.
    private static final int MES_PRUEBA = 1;
    private static final int ANIO_PRUEBA = 1901;

    @Test
    void generarPlanilla_debePersistirseYSeguirSiendoLegible_inclusoDespuesDeCerrarla() {
        // numero_documento tiene length=20 en la entidad: se usa un sufijo corto.
        String numeroDocumentoPrueba = "T" + System.currentTimeMillis();

        // ===== ARRANGE: parámetros legales, puesto, sistema de pensión y empleado =====
        // "codigo" es unique en parametros_legales, así que no se puede insertar un
        // segundo RMV/ASIGNACION_FAMILIAR si el ambiente ya los tiene cargados: se reutiliza
        // el existente y se usa su valor real en las aserciones, en vez de asumir 1130/113.
        asegurarParametroLegal("RMV", "Remuneración Mínima Vital (fixture de test)", new BigDecimal("1130.00"));
        BigDecimal asignacionFamiliarEsperada = asegurarParametroLegal(
                "ASIGNACION_FAMILIAR", "Asignación Familiar (fixture de test)", new BigDecimal("113.00")
        );

        Puesto puesto = puestoRepository.save(new Puesto(
                "Puesto de Integración " + System.nanoTime(),
                new BigDecimal("3000.00"), 8, null, null, null
        ));

        SistemaPension pension = sistemaPensionRepository.save(new SistemaPension(
                "ONP Test", "PUBLICO", new BigDecimal("0.13"), BigDecimal.ZERO
        ));

        Empleado empleado = new Empleado(
                "Integracion", "Test", numeroDocumentoPrueba,
                LocalDate.of(2020, 1, 1), puesto
        );
        empleado.actualizarDatosPersonales(
                TipoDocumento.DNI, LocalDate.of(1990, 1, 1),
                Sexo.M, EstadoCivil.Soltero, "Peruana"
        );
        empleado.actualizarRegimenLaboral(RegimenLaboral.GENERAL, true);
        empleado.asignarSistemaPension(pension);
        empleadoRepository.save(empleado);

        // ===== ACT 1: generar la planilla =====
        Planilla generada = planillaService.generarPlanilla(MES_PRUEBA, ANIO_PRUEBA, TipoPlanilla.MENSUAL);
        Long idPlanilla = generada.getIdPlanilla();

        assertThat(idPlanilla).isNotNull();
        DetallePlanilla miDetalle = buscarDetalleDelEmpleadoDePrueba(generada, numeroDocumentoPrueba);

        BigDecimal sueldoBaseEsperado = new BigDecimal("3000.00");
        BigDecimal sueldoBrutoEsperado = sueldoBaseEsperado.add(asignacionFamiliarEsperada);
        // 13% ONP sobre la remuneración computable completa (sueldo base + asignación
        // familiar): 3113.00 * 13% = 404.69.
        BigDecimal totalDescuentoEsperado = new BigDecimal("404.69");
        BigDecimal sueldoNetoEsperado = sueldoBrutoEsperado.subtract(totalDescuentoEsperado);

        assertThat(miDetalle.getSueldoBase()).isEqualByComparingTo(sueldoBaseEsperado);
        assertThat(miDetalle.getAsignacionFamiliar()).isEqualByComparingTo(asignacionFamiliarEsperada);
        assertThat(miDetalle.getSueldoBruto()).isEqualByComparingTo(sueldoBrutoEsperado);
        assertThat(miDetalle.getTotalDescuento()).isEqualByComparingTo(totalDescuentoEsperado);
        assertThat(miDetalle.getSueldoNeto()).isEqualByComparingTo(sueldoNetoEsperado);

        // ===== ACT 2: releer desde la base de datos =====
        // Antes del fix, esto lanzaba "La planilla es obligatoria" al reconstruir los
        // detalles desde las entidades (DetallePlanilla.reconstruir exigía planilla no nula).
        Planilla releida = planillaService.findById(idPlanilla);
        assertThat(releida.obtenerDetalles()).isNotEmpty();
        assertThat(buscarDetalleDelEmpleadoDePrueba(releida, numeroDocumentoPrueba).getPlanilla())
                .as("el detalle debe quedar vinculado a su planilla tras reconstruirse")
                .isNotNull();

        // ===== ACT 3: cerrar y releer ya cerrada =====
        // Antes del fix, esto lanzaba "La planilla está cerrada" al recargar los detalles
        // usando agregarDetalle() (una regla de negocio, no una operación de reconstrucción).
        Planilla cerrada = planillaService.cerrarPlanilla(idPlanilla);
        assertThat(cerrada.estaCerrada()).isTrue();

        Planilla releidaCerrada = planillaService.findById(idPlanilla);
        assertThat(releidaCerrada.estaCerrada()).isTrue();
        assertThat(releidaCerrada.obtenerDetalles()).isNotEmpty();
    }

    private DetallePlanilla buscarDetalleDelEmpleadoDePrueba(Planilla planilla, String numeroDocumento) {
        return planilla.obtenerDetalles().stream()
                .filter(d -> numeroDocumento.equals(d.getEmpleado().getNumeroDocumento()))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "No se encontró el detalle del empleado de prueba en la planilla"
                ));
    }

    // Reutiliza el parámetro legal si ya existe en el ambiente (codigo es unique) y
    // devuelve el valor vigente, para que las aserciones no dependan de datos hardcodeados.
    private BigDecimal asegurarParametroLegal(String codigo, String descripcion, BigDecimal valorPorDefecto) {
        return parametroLegalRepository.findTopByCodigoOrderByFechaInicioVigenciaDesc(codigo)
                .map(ParametroLegal::getValor)
                .orElseGet(() -> parametroLegalRepository.save(new ParametroLegal(
                        codigo, descripcion, valorPorDefecto, LocalDate.of(2024, 5, 1), null
                )).getValor());
    }
}
