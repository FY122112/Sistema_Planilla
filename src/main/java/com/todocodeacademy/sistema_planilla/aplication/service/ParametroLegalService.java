package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.ParamatroLegalServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.ParametroLegalRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.ParametroLegal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParametroLegalService implements ParamatroLegalServicePort {

    private final ParametroLegalRepositoryPort paraRepo;


    @Override
    public List<ParametroLegal> findAll() {
        return paraRepo.findAll();
    }

    @Override
    public ParametroLegal findById(Long id) {
        return paraRepo.findById(id).orElseThrow(() -> new RuntimeException("Parametro legal no encontrado"));
    }

    @Override
    public ParametroLegal save(ParametroLegal parametroLegal) {
        return paraRepo.save(parametroLegal);
    }

    @Override
    public ParametroLegal update(Long id, ParametroLegal parametroLegal) {
        ParametroLegal update = paraRepo.findById(id).orElseThrow(() -> new RuntimeException("Parametro legal no encontrado"));

        if (parametroLegal.getFechaInicioVigencia() != null) {
            update.actualizarDescripcion(parametroLegal.getDescripcion());
        }

        if (parametroLegal.getFechaInicioVigencia() != null) {
            update.actualizarFechaInicioVigencia(parametroLegal.getFechaInicioVigencia());
        }

        if (parametroLegal.getCodigo() != null) {
            update.actualizarCodigo(parametroLegal.getCodigo());
        }

        if (parametroLegal.getDescripcion() != null) {
            update.actualizarDescripcion(parametroLegal.getDescripcion());
        }
        if (parametroLegal.getValor() != null) {
            update.actualizarValor(parametroLegal.getValor());
        }

        update = paraRepo.save(update);

        return update;

    }

    @Override
    public void deleteById(Long id) {
        paraRepo.findById(id).orElseThrow(() -> new RuntimeException("Parametro legal no encontrado"));

        paraRepo.deleteById(id);
    }

    @Override
    public ParametroLegal findVigenteByCodigoAndFecha(String codigo, LocalDate fecha) {
        return paraRepo.findVigenteByCodigoAndFecha(codigo, fecha).orElseThrow(() -> new RuntimeException("Parametro legal por codigo y fecha no encontrado"));
    }

    @Override
    public ParametroLegal findTopByCodigoOrderByFechaInicioVigenciaDesc(String codigo) {
        return paraRepo.findTopByCodigoOrderByFechaInicioVigenciaDesc(codigo).orElseThrow(() -> new RuntimeException("Parametro legal por codigo no encontrado"));
    }

    @Override
    public List<ParametroLegal> findByCodigo(String codigo) {
        return paraRepo.findByCodigo(codigo);
    }

    @Override
    public List<ParametroLegal> findByDescripcionContainingIgnoreCase(String descripcion) {
        return paraRepo.findByDescripcionContainingIgnoreCase(descripcion);
    }

    @Override
    public List<ParametroLegal> findByFechaInicioVigenciaBetween(LocalDate startDate, LocalDate endDate) {
        return paraRepo.findByFechaInicioVigenciaBetween(startDate, endDate);
    }
}
