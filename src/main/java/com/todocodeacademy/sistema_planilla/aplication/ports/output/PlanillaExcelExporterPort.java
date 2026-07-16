package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.domain.model.Planilla;

public interface PlanillaExcelExporterPort {

    byte[] export(Planilla planilla);
}
