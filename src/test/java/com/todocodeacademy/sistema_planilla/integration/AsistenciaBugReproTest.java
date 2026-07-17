package com.todocodeacademy.sistema_planilla.integration;

import com.todocodeacademy.sistema_planilla.aplication.command.MarcarAsistneciaCommand;
import com.todocodeacademy.sistema_planilla.aplication.ports.input.AsistenciaServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpleadoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.PuestoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Asistencia;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoCivil;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.Sexo;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoDocumento;
import com.todocodeacademy.sistema_planilla.domain.model.Puesto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Reproduce el bug reportado el 2026-07-17: marcar "entrada" (una fila nueva de Asistencia,
 * insertada vía Hibernate persist()) lanzaba "El nombre es obligatorio" pese a que la fila
 * se guardaba correctamente.
 *
 * Causa: AsistenciaEntMapper#toEntity solo pone el id en el EmpleadoEntity de la asociación
 * (referencia por FK). Con persist() (fila nueva), Hibernate no resuelve esa referencia contra
 * la fila real de empleado, así que AsistenciaRepositoryAdapter#save devolvía una entidad cuyo
 * getEmpleado() tenía todos los campos en null salvo el id, y AsistenciaEntMapper#toDomain
 * fallaba al reconstruir el Empleado de dominio. Con merge() (fila existente, p. ej. "salida")
 * Hibernate sí resuelve la referencia, por eso el bug solo se veía en la primera marca del día.
 *
 * Fix: AsistenciaRepositoryAdapter#save restaura el Empleado completo desde el propio dominio
 * de entrada antes de mapear a domain, en vez de confiar en la referencia que deja Hibernate.
 */
@SpringBootTest
@Transactional
class AsistenciaBugReproTest {

    @Autowired
    private AsistenciaServicePort asistenciaService;

    @Autowired
    private EmpleadoRepositoryPort empleadoRepository;

    @Autowired
    private PuestoRepositoryPort puestoRepository;

    @Test
    void marcarEntrada_debeDevolverElEmpleadoCompleto_noSoloElId() {
        String numeroDocumentoPrueba = "R" + System.currentTimeMillis();

        Puesto puesto = puestoRepository.save(new Puesto(
                "Puesto Repro " + System.nanoTime(),
                new BigDecimal("3000.00"), 8, null, null, null
        ));

        Empleado empleado = new Empleado(
                "Repro", "Test", numeroDocumentoPrueba,
                LocalDate.of(2020, 1, 1), puesto
        );
        empleado.actualizarDatosPersonales(
                TipoDocumento.DNI, LocalDate.of(1990, 1, 1),
                Sexo.M, EstadoCivil.Soltero, "Peruana"
        );
        empleadoRepository.save(empleado);

        Asistencia entrada = asistenciaService.marcarAsistencia(
                new MarcarAsistneciaCommand(numeroDocumentoPrueba, "entrada")
        );

        assertThat(entrada.getIdAsistencia()).isNotNull();
        assertThat(entrada.getEmpleado().getNombre()).isEqualTo("Repro");
        assertThat(entrada.getEmpleado().getNumeroDocumento()).isEqualTo(numeroDocumentoPrueba);

        Asistencia salida = asistenciaService.marcarAsistencia(
                new MarcarAsistneciaCommand(numeroDocumentoPrueba, "salida")
        );

        assertThat(salida.getIdAsistencia()).isEqualTo(entrada.getIdAsistencia());
        assertThat(salida.getEmpleado().getNombre()).isEqualTo("Repro");
        assertThat(salida.tieneSalida()).isTrue();
    }
}
