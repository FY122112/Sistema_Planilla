package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.RoleEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.UsuarioSecEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUsuarioRepository extends JpaRepository<UsuarioSecEntity,Long> {

    Optional<UsuarioSecEntity> findByNombre(String nombre);

    Optional<UsuarioSecEntity> findByEmail(String email);

}
