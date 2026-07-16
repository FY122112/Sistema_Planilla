package com.todocodeacademy.sistema_planilla.aplication.ports.output;

import com.todocodeacademy.sistema_planilla.aplication.command.BoletaPdfData;

public interface BoletaPdfRendererPort {

    byte[] render(BoletaPdfData data);
}
