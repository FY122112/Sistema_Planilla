package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.ControlVacacionalRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.ControlVacacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Antes del fix, update() calculaba el delta (nuevo - actual) y lo pasaba directo a
 * asignarDias(), que exige un valor > 0. Reenviar el mismo valor (delta 0) o intentar
 * reducirlo rompía con "Los días son inválidos", un mensaje que no reflejaba el problema real.
 */
@ExtendWith(MockitoExtension.class)
class ControlVacacionalServiceTest {

    @Mock
    private ControlVacacionalRepositoryPort repository;

    @InjectMocks
    private ControlVacacionalService service;

    private ControlVacacion actual;

    @BeforeEach
    void setUp() {
        actual = ControlVacacion.reconstruir(
                1L,
                null,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                30,
                10,
                null,
                null
        );
    }

    @Test
    void update_debeSerIdempotente_cuandoDiasGanadosNoCambia() {
        when(repository.findById(1L)).thenReturn(Optional.of(actual));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ControlVacacion payload = ControlVacacion.reconstruir(
                null, null,
                actual.getFechaInicioPeriodo(), actual.getFechaFinPeriodo(),
                30, null, null, null
        );

        ControlVacacion resultado = service.update(1L, payload);

        assertThat(resultado.getDiasGanados()).isEqualTo(30);
    }

    @Test
    void update_debeIncrementarDiasGanados_cuandoElNuevoValorEsMayor() {
        when(repository.findById(1L)).thenReturn(Optional.of(actual));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ControlVacacion payload = ControlVacacion.reconstruir(
                null, null,
                actual.getFechaInicioPeriodo(), actual.getFechaFinPeriodo(),
                35, null, null, null
        );

        ControlVacacion resultado = service.update(1L, payload);

        assertThat(resultado.getDiasGanados()).isEqualTo(35);
    }

    @Test
    void update_debeLanzarExcepcionClara_cuandoIntentaReducirDiasGanados() {
        when(repository.findById(1L)).thenReturn(Optional.of(actual));

        ControlVacacion payload = ControlVacacion.reconstruir(
                null, null,
                actual.getFechaInicioPeriodo(), actual.getFechaFinPeriodo(),
                20, null, null, null
        );

        assertThatThrownBy(() -> service.update(1L, payload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("reducir");
    }
}
