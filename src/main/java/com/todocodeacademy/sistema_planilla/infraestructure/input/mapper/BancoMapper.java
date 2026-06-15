package com.todocodeacademy.sistema_planilla.infraestructure.input.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Banco;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Request.BancoRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.input.dto.Response.BancoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class BancoMapper {

    public BancoResponseDTO bancoResponseDTO(Banco banco) {

        return BancoResponseDTO.builder()
                .id(banco.getIdBanco())
                .nombreBanco(banco.getNombreBanco())
                .codigoBanco(banco.getCodigoBanco())
                .build();

    }

    public Banco bancoDTO(BancoRequestDTO banco) {

        Banco bank = Banco.crear(banco.getNombreBanco(), banco.getCodigoBanco());

        return bank;

    }

}
