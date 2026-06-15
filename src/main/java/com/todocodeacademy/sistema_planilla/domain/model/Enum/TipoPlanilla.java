package com.todocodeacademy.sistema_planilla.domain.model.Enum;

public enum TipoPlanilla {

    MENSUAL("Mensual"), // Puedes añadir más si necesitas
    CTS("CTS"), // Añadido
    GRATIFICACION("Gratificación"), // Añadido
    LIQUIDACION("Liquidación");

    private final String displayName; // Campo para el nombre legible

    TipoPlanilla(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
