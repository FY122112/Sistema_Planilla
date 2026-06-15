package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.SistemaPensionServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.SistemaPensionRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.SistemaPension;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SistemaPensionService implements SistemaPensionServicePort {

    private final SistemaPensionRepositoryPort repository;

    @Override
    public List<SistemaPension> findAll() {
        return repository.findAll();
    }

    @Override
    public SistemaPension findById(Long id) {
        return repository.findById(id).orElseThrow(()-> new IllegalArgumentException("Sistema de Pension no encontrada"));
    }

    @Override
    public SistemaPension save(SistemaPension sistemaPension) {
        return repository.save(sistemaPension);
    }

    @Override
    public SistemaPension update(Long id, SistemaPension sistemaPension) {
        SistemaPension update = repository.findById(id).orElseThrow(()-> new IllegalArgumentException("Sistema de Pension no encontrada"));

        if (sistemaPension.getNombre() != null) {

            update.actualizarNombre(sistemaPension.getNombre());

        }

        if (sistemaPension.getTipo() != null) {
            update.actualizarTipo(sistemaPension.getTipo());
        }

        if (sistemaPension.getPorcentajeAporte() != null) {
            update.actualizarPorcentajeAporte(sistemaPension.getPorcentajeAporte());

        }

        if (sistemaPension.getPorcentajeComision() != null) {

            update.actualizarPorcentajeComision(sistemaPension.getPorcentajeComision());
        }

        update =  repository.save(update);
        return update;

    }

    @Override
    public void deleteById(Long id) {
            repository.deleteById(id);
    }

    @Override
    public BigDecimal calcularDescuento(Long id, BigDecimal sueldo) {
        SistemaPension calculo = repository.findById(id).orElseThrow(()-> new IllegalArgumentException("Sistema de Pension no encontrada"));

        return calculo.calcularDescuento(sueldo);

    }
}
