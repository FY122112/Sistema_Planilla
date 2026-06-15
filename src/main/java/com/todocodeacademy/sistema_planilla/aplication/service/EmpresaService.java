package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.EmpresaServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpresaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Empresa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService implements EmpresaServicePort {

    private final EmpresaRepositoryPort repository;

    @Override
    public List<Empresa> findAll() {
        return repository.findAll();
    }

    @Override
    public Empresa findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Empresa no encontrada"));
    }

    @Override
    public Empresa save(Empresa empresa) {
        return repository.save(empresa);
    }

    @Override
    public Empresa update(Long id, Empresa empresa) {

        Empresa update = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Empresa no encontrada"));

        if (empresa.getDireccion() != null
                && empresa.getTelefono() != null
                && empresa.getCorreo() != null) {

            update.actualizarDatos(
                    empresa.getDireccion(),
                    empresa.getTelefono(),
                    empresa.getCorreo()
            );
        }

        return repository.save(update);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Empresa buscarPorRuc(String ruc) {
        return repository.findByRuc(ruc)
                .orElseThrow(() ->
                        new IllegalArgumentException("Empresa con ruc no encontrada"));
    }

    @Override
    public Empresa unicaEmpresa() {
        return null;
    }
}