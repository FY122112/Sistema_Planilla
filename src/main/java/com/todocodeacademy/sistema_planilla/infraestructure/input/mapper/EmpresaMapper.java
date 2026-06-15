package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;


import com.todocodeacademy.sistema_planilla.domain.model.Empresa;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.EmpresaRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.EmpresaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EmpresaMapper {

    public Empresa toEntity(
            EmpresaRequestDTO dto
    ) {

        Empresa empresa = new Empresa(
                dto.getRazonSocial(),
                dto.getRuc()
        );

        empresa.actualizarDatos(
                dto.getDireccion(),
                dto.getTelefono(),
                dto.getCorreo()
        );

        return empresa;
    }

    // =========================
    // ENTITY -> RESPONSE
    // =========================
    public EmpresaResponseDTO toResponseDTO(
            Empresa empresa
    ) {

        return new EmpresaResponseDTO(
                empresa.getIdEmpresa(),
                empresa.getRazonSocial(),
                empresa.getRuc(),
                empresa.getDireccion(),
                empresa.getTelefono(),
                empresa.getCorreo()
        );
    }
}