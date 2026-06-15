package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.PermissionEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface JpaPermisoRepository extends JpaRepository<PermissionEntity,Long> {

    Optional<PermissionEntity> findByName(String nombre);


}
