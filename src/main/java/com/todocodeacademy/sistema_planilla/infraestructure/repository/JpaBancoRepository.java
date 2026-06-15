package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.domain.model.Banco;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.BancoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaBancoRepository extends JpaRepository<BancoEntity, Long> {

    Optional<BancoEntity> findByNombreBanco(String nombreBanco);

    Optional<BancoEntity> findByCodigoBanco(String codigoBanco);

}
