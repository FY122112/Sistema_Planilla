package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.SolicitudAjusteServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.BoletaRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpleadoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.SolicitudAjusteRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoSolicitud;
import com.todocodeacademy.sistema_planilla.domain.model.SolicitudAjuste;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudAjusteService implements SolicitudAjusteServicePort {

    private final SolicitudAjusteRepositoryPort solicitudRepo;
    private final BoletaRepositoryPort boletaRepo;
    private final EmpleadoRepositoryPort empleadoRepo;

    @Override
    public SolicitudAjuste crear(Long idEmpleadoAutenticado, Long idBoleta, String mensaje) {

        Boleta boleta = boletaRepo.findById(idBoleta)
                .orElseThrow(() -> new IllegalArgumentException("Boleta no encontrada"));

        boolean esPropia = boletaRepo.findByEmpleadoId(idEmpleadoAutenticado).stream()
                .anyMatch(b -> b.getIdBoleta().equals(idBoleta));

        if (!esPropia) {
            throw new AccessDeniedException("No puedes reportar un reclamo sobre la boleta de otro empleado");
        }

        Empleado empleado = empleadoRepo.findById(idEmpleadoAutenticado)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

        return solicitudRepo.save(new SolicitudAjuste(empleado, boleta, mensaje));
    }

    @Override
    public List<SolicitudAjuste> findAll() {
        return solicitudRepo.findAll();
    }

    @Override
    public List<SolicitudAjuste> findByEmpleadoId(Long idEmpleado) {
        return solicitudRepo.findByEmpleadoId(idEmpleado);
    }

    @Override
    public SolicitudAjuste marcarComoAtendida(Long idSolicitud) {

        SolicitudAjuste solicitud = solicitudRepo.findById(idSolicitud)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        solicitud.marcarComoAtendida();

        return solicitudRepo.save(solicitud);
    }

    @Override
    public long contarPendientes() {
        return solicitudRepo.countByEstado(EstadoSolicitud.PENDIENTE);
    }
}
