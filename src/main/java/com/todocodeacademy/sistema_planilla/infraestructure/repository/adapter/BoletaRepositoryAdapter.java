package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.BoletaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaBoletaRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BoletaRepositoryAdapter implements BoletaRepositoryPort {

    private final JpaBoletaRepository repository;

    @Override
    public List<Boleta> findAll() {
        return List.of();
    }

    @Override
    public Optional<Boleta> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Boleta save(Boleta banco) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Optional<Boleta> getBoletaByDetallePlanilla(DetallePlanilla detallePlanilla) {
        return Optional.empty();
    }

    @Override
    public List<Boleta> getBoletasByFechaEmisionBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return List.of();
    }

    @Override
    public List<Boleta> getBoletasByFirmadaStatus(Boolean firmada) {
        return List.of();
    }

    @Override
    public List<Boleta> getBoletasByEnviadaStatus(Boolean enviada) {
        return List.of();
    }
}
