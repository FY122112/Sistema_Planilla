package com.todocodeacademy.sistema_planilla.domain.model.Enum;

public enum TipoDocumento {

    DNI("Documento Nacional de Identidad"),
    CE("Carnet de Extranjería"),
    PAS("Pasaporte"),
    RUC("Registro Único de Contribuyentes"),
    CEX("Cédula de Identidad de Extranjero"); // Añadir más si es necesario
    private final String descripcion;

    TipoDocumento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
