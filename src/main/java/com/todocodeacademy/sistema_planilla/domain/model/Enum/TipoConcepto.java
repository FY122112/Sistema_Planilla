package com.todocodeacademy.sistema_planilla.domain.model.Enum;

public enum TipoConcepto {

    INGRESO("Ingreso"),
    DESCUENTO("Descuento"),
    APORTE_EMPLEADOR("Aporte Empleador"); // <-- Aquí se añaden los nombres a mostrar

    private final String displayName; // <-- Nueva propiedad

    TipoConcepto(String displayName) { // <-- Nuevo constructor
        this.displayName = displayName;
    }

    public String getDisplayName() { // <-- Nuevo método getter
        return displayName;
    }
}
