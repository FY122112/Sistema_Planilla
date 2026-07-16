package com.todocodeacademy.sistema_planilla.infraestructure.email;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmailSenderPort;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSenderAdapter implements EmailSenderPort {

    private final JavaMailSender mailSender;

    @Override
    public void enviarNotificacionBoletaGenerada(
            String destinatario,
            String nombreEmpleado,
            String periodo,
            String enlacePortal,
            byte[] adjuntoPdf,
            String nombreArchivoPdf
    ) {

        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

            helper.setTo(destinatario);
            helper.setSubject("Tu boleta de pago de " + periodo + " ya está disponible");
            helper.setText(
                    "Hola " + nombreEmpleado + ",\n\n" +
                            "Tu boleta de pago de " + periodo + " ya fue generada. La encuentras adjunta en PDF.\n\n" +
                            "También puedes revisarla en tu portal de autoservicio:\n" + enlacePortal + "\n\n" +
                            "Sistema Planilla — TextiLima SAC"
            );

            if (adjuntoPdf != null && adjuntoPdf.length > 0) {
                helper.addAttachment(nombreArchivoPdf, new ByteArrayResource(adjuntoPdf));
            }

            mailSender.send(mensaje);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo armar el correo de notificación", e);
        }
    }
}
