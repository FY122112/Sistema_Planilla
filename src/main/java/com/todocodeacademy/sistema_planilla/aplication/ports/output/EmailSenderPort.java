package com.todocodeacademy.sistema_planilla.aplication.ports.output;

public interface EmailSenderPort {

    void enviarNotificacionBoletaGenerada(
            String destinatario,
            String nombreEmpleado,
            String periodo,
            String enlacePortal,
            byte[] adjuntoPdf,
            String nombreArchivoPdf
    );
}
