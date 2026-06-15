package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Permission;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.PermissionRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.PermissionResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    public Permission toDomain(PermissionRequestDTO request) {

         Permission permission = Permission.reconstruir(
                 null,
                 request.getName(),
                 request.getDescription(),
                 null,
                 null
         );

         return permission;

               /* Permission.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();*/
    }

    public PermissionResponseDTO toResponse(Permission permission) {

        return PermissionResponseDTO.builder()
                .idPermission(permission.getIdPermission())
                .name(permission.getName())
                .description(permission.getDescription())
                .build();
    }

}
