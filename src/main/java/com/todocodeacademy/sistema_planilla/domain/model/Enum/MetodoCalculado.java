package com.todocodeacademy.sistema_planilla.domain.model.Enum;

public enum MetodoCalculado {
    FIJO("Fijo"),
    PORCENTAJE("Porcentaje");

    private final String displayName;

    MetodoCalculado(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MetodoCalculado fromDisplayName(String displayName) {
        for (MetodoCalculado mc : values()) {
            if (mc.displayName.equalsIgnoreCase(displayName)) {
                return mc;
            }
        }
        throw new IllegalArgumentException("MetodoCalculado inválido: " + displayName);
    }
}
