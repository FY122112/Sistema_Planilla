package com.todocodeacademy.sistema_planilla.domain.model.Enum;

public enum TipoConcepto {
    INGRESO("Ingreso"),
    DESCUENTO("Descuento"),
    APORTE_EMPLEADOR("Aporte Empleador");

    private final String displayName;

    TipoConcepto(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TipoConcepto fromDisplayName(String displayName) {
        for (TipoConcepto tc : values()) {
            if (tc.displayName.equalsIgnoreCase(displayName)) {
                return tc;
            }
        }
        throw new IllegalArgumentException("TipoConcepto inválido: " + displayName);
    }
}
