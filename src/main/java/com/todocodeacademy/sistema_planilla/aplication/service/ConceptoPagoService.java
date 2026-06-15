package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.ConceptoPagoServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.ConceptoPagoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.ConceptoPago;
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
        ConceptoPago concepto = conRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("Concepto no encontrado"));

        return concepto;

    }

    @Override
    public ConceptoPago save(ConceptoPago conceptoPago) {
        return conRepo.save(conceptoPago);
    }

    @Override
    public ConceptoPago update(Long id, ConceptoPago conceptoPago) {
        ConceptoPago concepto = conRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("Concepto no encontrado"));



            if (conceptoPago.getNombreConcepto() != null) {

                concepto.actualizarNombre(conceptoPago.getNombreConcepto());
            }
            if (conceptoPago.getValorReferencial() != null) {

                concepto.actualizarValorReferencial(conceptoPago.getValorReferencial());
            }


        ConceptoPago actualizado = conRepo.save(concepto);

        return actualizado;

    }

    @Override
    public void deleteById(Long id) {

        conRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("Concepto no encontrado"));

        conRepo.deleteById(id);

    }




}
