package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.BancoServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.BancoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Banco;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.BancoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BancoService implements BancoServicePort {

    private final BancoRepositoryPort banRepo;

    @Override
    public List<Banco> findAll() {
        return banRepo.findAll();
    }

    @Override
    public Banco findById(Long id) {

        Banco banco = banRepo.findById(id).orElseThrow( ()-> new RuntimeException("Banco No encontrado") );

        return banco;
    }

    @Override
    public Banco save(Banco banco) {

        return  banRepo.save(banco);
    }

    @Override
    public Banco update(Long id, Banco banco) {
        Banco bank = banRepo.findById(id).orElseThrow( ()-> new RuntimeException("Banco No encontrado") );

        if (banco.getNombreBanco() != null) {

            bank.actualizarNombre(banco.getNombreBanco());

        }

        Banco updateBank = banRepo.save(bank);

        return updateBank;
    }

    @Override
    public void deleteById(Long id) {

        banRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Banco No encontrado"));

        banRepo.deleteById(id);
    }
}
