package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.BoletaServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.BoletaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoletaService implements BoletaServicePort {

    private final BoletaRepositoryPort boleRepo;

    @Override
    public List<Boleta> findAll() {
        return boleRepo.findAll();
    }

    @Override
    public Boleta findById(Long id) {
        Boleta boleta =  boleRepo.findById(id).orElseThrow( ()-> new IllegalArgumentException("Boleta no encontrado"));

        return boleta;

    }

    @Override
    public Boleta save(Boleta boleta) {
        return boleRepo.save(boleta);
    }

    @Override
    public Boleta update(Long id,Boleta boleta) {
        Boleta actual = boleRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Boleta no encontrada"));

        if (boleta.getRutaPdf() != null) {
            actual.asignarRutaPdf(boleta.getRutaPdf());
        }

        if (boleta.getEstadoBoleta() != null) {
            switch (boleta.getEstadoBoleta()) {
                case PAGADA -> actual.marcarComoPagada();
                case FIRMADA -> actual.marcarComoFirmada();
                case ENVIADA -> actual.marcarComoEnviada();
                case GENERADA -> actual.marcarComoGenerada();
            }
        }

        if (boleta.getSueldoBruto() != null || boleta.getTotalDescuento() != null) {
            actual.actualizarMontos(
                    boleta.getSueldoBruto(),
                    boleta.getTotalDescuento()
            );
        }

        return boleRepo.save(actual);

    }

    @Override
    public void deleteById(Long id) {

        Boleta boleta =  boleRepo.findById(id).orElseThrow( ()-> new IllegalArgumentException("Boleta no encontrado"));

        boleRepo.deleteById(id);


    }
}
