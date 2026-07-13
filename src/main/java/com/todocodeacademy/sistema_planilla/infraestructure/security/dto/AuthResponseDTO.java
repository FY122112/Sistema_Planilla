package com.todocodeacademy.sistema_planilla.infraestructure.security.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username,message,jwt,status"})
public record AuthResponseDTO(String userName,String message,String jwt,boolean status) {
}
