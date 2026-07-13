package com.todocodeacademy.sistema_planilla.domain.model.Enum;

public enum TipoSistemaPensiones {
    AFP("AFP"),
    ONP("ONP");

    private final String displayName;

    TipoSistemaPensiones(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TipoSistemaPensiones fromDisplayName(String displayName) {
        for (TipoSistemaPensiones tsp : values()) {
            if (tsp.displayName.equalsIgnoreCase(displayName)) {
                return tsp;
            }
        }
        throw new IllegalArgumentException("TipoSistemaPensiones inválido: " + displayName);
    }
}
