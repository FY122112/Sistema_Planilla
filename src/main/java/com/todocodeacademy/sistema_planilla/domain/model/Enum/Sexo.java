package com.todocodeacademy.sistema_planilla.domain.model.Enum;

public enum Sexo {

    M("M"),
    F("F");
    private final String descripcion;

    Sexo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
