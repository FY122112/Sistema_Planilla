package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.BoletaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoBoleta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Antes del fix, BoletaService.update solo distinguía PAGADA de "todo lo demás",
 * así que pedir FIRMADA o ENVIADA terminaba dejando la boleta en GENERADA.
 * Estos tests verifican que las 4 transiciones de EstadoBoleta se apliquen tal cual se piden.
 */
@ExtendWith(MockitoExtension.class)
class BoletaServiceTest {

    @Mock
    private BoletaRepositoryPort boletaRepositoryPort;

    @InjectMocks
    private BoletaService boletaService;

    private Boleta actual;

    @BeforeEach
    void setUp() {
        actual = Boleta.reconstruir(
                1L,
                null,
                LocalDate.of(2026, 7, 31),
                7,
                2026,
                new BigDecimal("3000.00"),
                new BigDecimal("500.00"),
                new BigDecimal("2500.00"),
                null,
                EstadoBoleta.GENERADA,
                null,
                null
        );
    }

    @ParameterizedTest
    @EnumSource(EstadoBoleta.class)
    void update_debeAplicarElEstadoSolicitado_paraCadaEstadoPosible(EstadoBoleta estadoSolicitado) {
        when(boletaRepositoryPort.findById(1L)).thenReturn(Optional.of(actual));
        when(boletaRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Boleta payload = Boleta.reconstruir(
                null, null, null, null, null,
                actual.getSueldoBruto(), actual.getTotalDescuento(), null,
                null, estadoSolicitado, null, null
        );

        Boleta resultado = boletaService.update(1L, payload);

        assertThat(resultado.getEstadoBoleta()).isEqualTo(estadoSolicitado);
    }

    @Test
    void update_debeActualizarSoloRutaPdf_cuandoEstadoSolicitadoEsElMismoQueElActual() {
        when(boletaRepositoryPort.findById(1L)).thenReturn(Optional.of(actual));
        when(boletaRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Boleta payload = Boleta.reconstruir(
                null, null, null, null, null,
                actual.getSueldoBruto(), actual.getTotalDescuento(), null,
                "boletas/2026-07/emp1.pdf", EstadoBoleta.GENERADA, null, null
        );

        Boleta resultado = boletaService.update(1L, payload);

        assertThat(resultado.getEstadoBoleta()).isEqualTo(EstadoBoleta.GENERADA);
        assertThat(resultado.getRutaPdf()).isEqualTo("boletas/2026-07/emp1.pdf");
    }
}
