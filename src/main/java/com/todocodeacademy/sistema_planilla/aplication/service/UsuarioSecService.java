package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.ports.input.UsuarioSecServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.UsuarioSecRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.UsuarioSec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioSecService implements UsuarioSecServicePort {

    private final UsuarioSecRepositoryPort repository;

    @Override
    public List<UsuarioSec> findAll() {

        return repository.findAll();
    }

    @Override
    public UsuarioSec findById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        ));
    }

    @Override
    public UsuarioSec save(UsuarioSec usuarioSec) {

        repository.findByUsername(usuarioSec.getUsername())
                .ifPresent(usuario -> {
                    throw new IllegalArgumentException(
                            "El username ya existe"
                    );
                });

        repository.findByEmail(usuarioSec.getEmail())
                .ifPresent(usuario -> {
                    throw new IllegalArgumentException(
                            "El email ya existe"
                    );
                });

        return repository.save(usuarioSec);
    }

    @Override
    public UsuarioSec update(Long id, UsuarioSec usuarioSec) {

        UsuarioSec actual = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        ));

        if (usuarioSec.getUsername() != null) {

            repository.findByUsername(usuarioSec.getUsername())
                    .ifPresent(existente -> {

                        if (!existente.getIdUsuario().equals(id)) {

                            throw new IllegalArgumentException(
                                    "El username ya existe"
                            );
                        }
                    });

            actual.actualizarUsername(
                    usuarioSec.getUsername()
            );
        }

        if (usuarioSec.getEmail() != null) {

            repository.findByEmail(usuarioSec.getEmail())
                    .ifPresent(existente -> {

                        if (!existente.getIdUsuario().equals(id)) {

                            throw new IllegalArgumentException(
                                    "El email ya existe"
                            );
                        }
                    });

            actual.actualizarEmail(
                    usuarioSec.getEmail()
            );
        }

        if (usuarioSec.getPassword() != null) {

            actual.actualizarPassword(
                    usuarioSec.getPassword()
            );
        }

        return repository.save(actual);
    }

    @Override
    public void deleteById(Long id) {

        repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        ));

        repository.deleteById(id);
    }

    @Override
    public UsuarioSec findByUsername(String username) {

        return repository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        ));
    }

    @Override
    public UsuarioSec findByEmail(String email) {

        return repository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        ));
    }
}