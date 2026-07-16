package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.SolicitudAjusteRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoSolicitud;
import com.todocodeacademy.sistema_planilla.domain.model.SolicitudAjuste;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.SolicitudAjusteEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaSolicitudAjusteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SolicitudAjusteRepositoryAdapter implements SolicitudAjusteRepositoryPort {

    private final JpaSolicitudAjusteRepository repository;
    private final SolicitudAjusteEntMapper mapper;

    @Override
    public List<SolicitudAjuste> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .sorted(Comparator.comparing(SolicitudAjuste::getFechaCreacion).reversed())
                .toList();
    }

    @Override
    public Optional<SolicitudAjuste> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public SolicitudAjuste save(SolicitudAjuste solicitud) {

        var entity = mapper.toEntity(solicitud);
        var savedEntity = repository.save(entity);

        var completa = repository.findById(savedEntity.getIdSolicitud())
                .orElseThrow(() -> new RuntimeException("Error al recuperar la solicitud"));

        return mapper.toDomain(completa);
    }

    @Override
    public List<SolicitudAjuste> findByEmpleadoId(Long idEmpleado) {
        return repository.findByEmpleado_IdEmpleadoOrderByFechaCreacionDesc(idEmpleado)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public long countByEstado(EstadoSolicitud estado) {
        return repository.countByEstado(estado);
    }
}
