package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.BancoEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRoleRepository extends JpaRepository<RoleEntity,Long> {

    Optional<RoleEntity> findByName(String nombre);

}
