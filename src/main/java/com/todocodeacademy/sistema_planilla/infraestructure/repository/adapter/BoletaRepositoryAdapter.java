package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.BoletaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoBoleta;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.DetallePlanillaEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.BoletaEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.DetallePlanillaEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaBoletaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoletaRepositoryAdapter implements BoletaRepositoryPort {

    private final JpaBoletaRepository repository;
    private final BoletaEntMapper mapper;
    private final DetallePlanillaEntMapper detMapper;

    @Override
    public List<Boleta> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Boleta> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Boleta save(Boleta boleta) {
        return mapper.toDomain(repository.save(mapper.toEntity(boleta)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Boleta> getBoletaByDetallePlanilla(DetallePlanilla detallePlanilla) {
        // Convertimos DetallePlanilla → DetallePlanillaEntity antes de consultar
        DetallePlanillaEntity detalleEntity = detMapper.toEntity(detallePlanilla);
        return repository.getBoletaByDetallePlanilla(detalleEntity).map(mapper::toDomain);
    }

    @Override
    public List<Boleta> getBoletasByFechaEmisionBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return repository.getBoletasByFechaEmisionBetween(fechaInicio, fechaFin)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Boleta> getBoletasByEstadoBoleta(EstadoBoleta estadoBoleta) {
        return repository.getBoletasByEstadoBoleta(estadoBoleta)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Boleta> findByEmpleadoId(Long idEmpleado) {
        return repository.findByDetallePlanilla_Empleado_IdEmpleado(idEmpleado)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Boleta> findByPeriodo(Integer periodoMes, Integer periodoAnio) {
        return repository.findByPeriodoMesAndPeriodoAnio(periodoMes, periodoAnio)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}

