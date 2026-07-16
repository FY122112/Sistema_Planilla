package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.BancoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.PuestoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.SistemaPensionRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.RegimenLaboral;
import com.todocodeacademy.sistema_planilla.domain.model.Puesto;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.UpdateEmpleadoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Antes del fix, el update de tieneHijosCalificados dependía de que regimenLaboral
 * también viniera en el mismo request (estaba anidado dentro de su "if"). Si el cliente
 * solo quería cambiar tieneHijosCalificados, el cambio se perdía en silencio.
 */
@ExtendWith(MockitoExtension.class)
class EmpleadoMapperTest {

    @Mock
    private PuestoRepositoryPort puestoRepository;

    @Mock
    private BancoRepositoryPort bancoRepository;

    @Mock
    private SistemaPensionRepositoryPort sistemaPensionRepository;

    @InjectMocks
    private EmpleadoMapper empleadoMapper;

    private Empleado empleadoExistente() {
        Puesto puesto = new Puesto(
                "Operario", new BigDecimal("1800.00"), 8, null, null, null
        );

        Empleado empleado = new Empleado(
                "Ana", "Torres", "87654321",
                LocalDate.of(2022, 3, 1), puesto
        );

        empleado.actualizarRegimenLaboral(RegimenLaboral.GENERAL, false);

        return empleado;
    }

    private UpdateEmpleadoRequest requestConSoloTieneHijosCalificados(boolean valor) {
        return new UpdateEmpleadoRequest(
                null, null, null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, null, null,
                null,
                null, valor,
                null, null,
                null, null, null
        );
    }

    @Test
    void updateDomain_debeActualizarTieneHijosCalificados_aunqueNoVengaRegimenLaboral() {
        Empleado actual = empleadoExistente();

        Empleado actualizado = empleadoMapper.updateDomain(
                requestConSoloTieneHijosCalificados(true),
                actual
        );

        assertThat(actualizado.isTieneHijosCalificados()).isTrue();
        assertThat(actualizado.getRegimenLaboral()).isEqualTo(RegimenLaboral.GENERAL);
    }

    @Test
    void updateDomain_debeMantenerRegimenLaboralActual_cuandoRequestNoLoTrae() {
        Empleado actual = empleadoExistente();

        Empleado actualizado = empleadoMapper.updateDomain(
                requestConSoloTieneHijosCalificados(false),
                actual
        );

        assertThat(actualizado.getRegimenLaboral()).isEqualTo(RegimenLaboral.GENERAL);
        assertThat(actualizado.isTieneHijosCalificados()).isFalse();
    }
}
