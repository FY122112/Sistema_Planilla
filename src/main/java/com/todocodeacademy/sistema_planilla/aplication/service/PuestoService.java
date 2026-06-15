package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.PuestoServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.PuestoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Puesto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PuestoService implements PuestoServicePort {

    private final PuestoRepositoryPort  puRepo;

    @Override
    public List<Puesto> findAll() {
        return puRepo.findAll();
    }

    @Override
    public Puesto findById(Long id) {
        return puRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("Puesto no encontrado"));
    }

    @Override
    public Puesto save(Puesto puesto) {
        Puesto guardar = puRepo.save(puesto);

        return guardar;

    }

    @Override
    public Puesto update(Long id, Puesto puesto) {
        Puesto update = puRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("Puesto no encontrado"));

        if (puesto.getNombre() != null) {
            update.actualizarNombre(puesto.getNombre());
        }

        if (puesto.getSalarioBase() != null) {
            update.actualizarSalarioBase(puesto.getSalarioBase());
        }

        if (puesto.getDescripcion() != null) {
            update.actualizarDescripcion(puesto.getDescripcion());
        }

        if (puesto.getJornadaLaboralHoras() != null) {
            update.actualizarJornadaLaboralHoras(puesto.getJornadaLaboralHoras());
        }

        if (puesto.getHoraInicioJornada() != null) {
            update.actualizarHoraInicioJornada(puesto.getHoraInicioJornada());
        }

        if (puesto.getHoraFinJornada() != null) {
            update.actualizarHoraFinJornada(puesto.getHoraFinJornada());
        }

        update = puRepo.save(update);

        return update;
    }

    @Override
    public void deleteById(Long id) {
        puRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("Puesto no encontrado"));

        puRepo.deleteById(id);
    }

    @Override
    public Puesto findByNombrePuesto(String name) {
        return puRepo.findByNombrePuesto(name).orElseThrow(()-> new IllegalArgumentException("Puesto por nombre no encontrado"));
    }

    @Override
    public List<Puesto> findBySalarioBaseGreaterThanEqual(BigDecimal salarioBase) {
        return puRepo.findBySalarioBaseGreaterThanEqual(salarioBase);
    }


}
