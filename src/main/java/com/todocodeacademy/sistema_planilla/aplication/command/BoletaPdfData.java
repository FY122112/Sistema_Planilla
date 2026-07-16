package com.todocodeacademy.sistema_planilla.aplication.command;

import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Empresa;

public record BoletaPdfData(
        Boleta boleta,
        DetallePlanilla detalle,
        Empresa empresa
) {
}
