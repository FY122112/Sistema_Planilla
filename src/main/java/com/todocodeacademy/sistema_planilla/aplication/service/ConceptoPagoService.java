package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.ConceptoPagoServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.ConceptoPagoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.ConceptoPago;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoConcepto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConceptoPagoService implements ConceptoPagoServicePort {

    private final ConceptoPagoRepositoryPort conRepo;

    @Override
    public List<ConceptoPago> findAll() {
        return conRepo.findAll();
    }

    @Override
    public ConceptoPago findById(Long id) {

        return conRepo.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Concepto no encontrado"));
    }

    @Override
    public ConceptoPago save(ConceptoPago conceptoPago) {
        return conRepo.save(conceptoPago);
    }

    @Override
    public ConceptoPago update(Long id, ConceptoPago conceptoPago) {

        ConceptoPago concepto = conRepo.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Concepto no encontrado"));

        if (conceptoPago.getNombreConcepto() != null) {
            concepto.actualizarNombre(conceptoPago.getNombreConcepto());
        }

        if (conceptoPago.getValorReferencial() != null) {
            concepto.actualizarValorReferencial(
                    conceptoPago.getValorReferencial()
            );
        }

        if (conceptoPago.getDescripcion() != null) {
            concepto.actualizarDescripcion(
                    conceptoPago.getDescripcion()
            );
        }

        return conRepo.save(concepto);
    }

    @Override
    public void deleteById(Long id) {

        conRepo.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Concepto no encontrado"));

        conRepo.deleteById(id);
    }

    // ===============================
    // NUEVOS MÉTODOS
    // ===============================

    @Override
    public ConceptoPago findByNombreConcepto(String nombreConcepto) {

        return conRepo.findByNombreConcepto(nombreConcepto)
                .orElseThrow(() ->
                        new IllegalArgumentException("Concepto no encontrado"));
    }

    @Override
    public ConceptoPago findByNombreConceptoAndTipo(
            String nombreConcepto,
            TipoConcepto tipo
    ) {

        return conRepo.findByNombreConceptoAndTipo(
                        nombreConcepto,
                        tipo
                )
                .orElseThrow(() ->
                        new IllegalArgumentException("Concepto no encontrado"));
    }

    @Override
    public List<ConceptoPago> findByTipo(TipoConcepto tipo) {
        return conRepo.findByTipo(tipo);
    }

    @Override
    public List<ConceptoPago> findByAfectoEssalud(
            Boolean afectoEssalud
    ) {
        return conRepo.findByAfectoEssalud(afectoEssalud);
    }
}