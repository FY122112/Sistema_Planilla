package com.todocodeacademy.sistema_planilla.domain.model.Enum;

public enum EstadoAsistencia {

    PRESENTE("Presente"),
    TARDANZA("Tardanza"),
    AUSENTE("Ausente"),
    FALTA_JUSTIFICADA("Falta Justificada"),
    PERMISO("Permiso"),
    TARDANZA_JUSTIFICADA("Tardanza Justificada"),
    FALTA("Falta"),
    LICENCIA("Licencia");


    private final String displayName;

    EstadoAsistencia(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
