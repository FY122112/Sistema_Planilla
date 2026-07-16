package com.todocodeacademy.sistema_planilla.aplication.ports.input;

import com.todocodeacademy.sistema_planilla.domain.model.SolicitudAjuste;

import java.util.List;

public interface SolicitudAjusteServicePort {

    SolicitudAjuste crear(Long idEmpleadoAutenticado, Long idBoleta, String mensaje);

    List<SolicitudAjuste> findAll();

    List<SolicitudAjuste> findByEmpleadoId(Long idEmpleado);

    SolicitudAjuste marcarComoAtendida(Long idSolicitud);

    long contarPendientes();
}
