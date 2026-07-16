package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.command.BoletaPdfData;
import com.todocodeacademy.sistema_planilla.aplication.ports.input.BoletaServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.BoletaPdfRendererPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.BoletaRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.DetallePlanillaRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmailSenderPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpresaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.domain.model.Empresa;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoBoleta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoletaService implements BoletaServicePort {

    private final BoletaRepositoryPort boleRepo;
    private final DetallePlanillaRepositoryPort detallePlanillaRepo;
    private final EmpresaRepositoryPort empresaRepo;
    private final BoletaPdfRendererPort pdfRenderer;
    private final EmailSenderPort emailSender;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public List<Boleta> findAll() {
        return boleRepo.findAll();
    }

    @Override
    public Boleta findById(Long id) {
        Boleta boleta =  boleRepo.findById(id).orElseThrow( ()-> new IllegalArgumentException("Boleta no encontrado"));

        return boleta;

    }

    @Override
    public Boleta save(Boleta boleta) {

        Boleta guardada = boleRepo.save(boleta);

        enviarNotificacionSiCorresponde(guardada);

        return guardada;
    }

    // El envío es un efecto secundario "best effort": un problema de SMTP nunca debe
    // impedir que la boleta se guarde, así que cualquier falla solo se loguea.
    private void enviarNotificacionSiCorresponde(Boleta boleta) {

        try {
            DetallePlanilla detalle = boleta.getDetallePlanilla() != null
                    ? detallePlanillaRepo.findById(boleta.getDetallePlanilla().getIdDetalle()).orElse(null)
                    : null;

            Empleado empleado = detalle != null ? detalle.getEmpleado() : null;

            if (empleado == null || empleado.getCorreo() == null || empleado.getCorreo().isBlank()) {
                return;
            }

            String enlace = frontendUrl + "/mi-portal/boletas/" + boleta.getIdBoleta();
            byte[] pdf = renderPdf(boleta, detalle);
            String nombreArchivoPdf = "boleta-%s-%s-%s.pdf".formatted(
                    empleado.getNumeroDocumento(), boleta.getPeriodoMes(), boleta.getPeriodoAnio()
            );

            emailSender.enviarNotificacionBoletaGenerada(
                    empleado.getCorreo(),
                    empleado.getNombreCompleto(),
                    periodoTexto(boleta),
                    enlace,
                    pdf,
                    nombreArchivoPdf
            );
        } catch (Exception e) {
            log.warn("No se pudo enviar la notificación de la boleta {} generada", boleta.getIdBoleta(), e);
        }
    }

    private byte[] renderPdf(Boleta boleta, DetallePlanilla detalle) {
        Empresa empresa = empresaRepo.findAll().stream().findFirst().orElse(null);
        return pdfRenderer.render(new BoletaPdfData(boleta, detalle, empresa));
    }

    private String periodoTexto(Boleta boleta) {
        String mes = Month.of(boleta.getPeriodoMes()).getDisplayName(TextStyle.FULL, new Locale("es"));
        return Character.toUpperCase(mes.charAt(0)) + mes.substring(1) + " " + boleta.getPeriodoAnio();
    }

    @Override
    public Boleta update(Long id,Boleta boleta) {
        Boleta actual = boleRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Boleta no encontrada"));

        if (boleta.getRutaPdf() != null) {
            actual.asignarRutaPdf(boleta.getRutaPdf());
        }

        if (boleta.getEstadoBoleta() != null) {
            switch (boleta.getEstadoBoleta()) {
                case PAGADA -> actual.marcarComoPagada();
                case FIRMADA -> actual.marcarComoFirmada();
                case ENVIADA -> actual.marcarComoEnviada();
                case GENERADA -> actual.marcarComoGenerada();
            }
        }

        if (boleta.getSueldoBruto() != null || boleta.getTotalDescuento() != null) {
            actual.actualizarMontos(
                    boleta.getSueldoBruto(),
                    boleta.getTotalDescuento()
            );
        }

        return boleRepo.save(actual);

    }

    @Override
    public void deleteById(Long id) {

        Boleta boleta =  boleRepo.findById(id).orElseThrow( ()-> new IllegalArgumentException("Boleta no encontrado"));

        boleRepo.deleteById(id);


    }

    @Override
    public byte[] generarPdf(Long id) {

        Boleta boleta = findById(id);

        DetallePlanilla detalle = boleta.getDetallePlanilla() != null
                ? detallePlanillaRepo.findById(boleta.getDetallePlanilla().getIdDetalle()).orElse(null)
                : null;

        return renderPdf(boleta, detalle);
    }

    @Override
    public List<Boleta> findByEmpleadoId(Long idEmpleado) {
        return boleRepo.findByEmpleadoId(idEmpleado);
    }

    @Override
    public Boleta firmarComoEmpleado(Long idBoleta, Long idEmpleadoAutenticado) {

        Boleta boleta = findById(idBoleta);

        DetallePlanilla detalle = boleta.getDetallePlanilla() != null
                ? detallePlanillaRepo.findById(boleta.getDetallePlanilla().getIdDetalle()).orElse(null)
                : null;

        Long idEmpleadoDeLaBoleta = detalle != null ? detalle.getEmpleado().getIdEmpleado() : null;

        if (idEmpleadoAutenticado == null || !idEmpleadoAutenticado.equals(idEmpleadoDeLaBoleta)) {
            throw new AccessDeniedException("No puedes firmar la boleta de otro empleado");
        }

        if (boleta.getEstadoBoleta() != EstadoBoleta.GENERADA) {
            throw new IllegalStateException("Solo se puede confirmar conformidad sobre una boleta recién generada");
        }

        boleta.marcarComoFirmada();

        return boleRepo.save(boleta);
    }

    @Override
    public byte[] exportarZip(Integer periodoMes, Integer periodoAnio) {

        List<Boleta> boletas = boleRepo.findByPeriodo(periodoMes, periodoAnio);
        // Si un mismo empleado terminara con más de una boleta en el período (no debería
        // pasar en uso normal, pero el nombre de archivo es por DNI), se desambigua con el
        // id de boleta en vez de reventar por "duplicate entry" en el zip.
        Set<String> nombresUsados = new HashSet<>();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try (ZipOutputStream zip = new ZipOutputStream(out)) {
                for (Boleta boleta : boletas) {

                    DetallePlanilla detalle = boleta.getDetallePlanilla() != null
                            ? detallePlanillaRepo.findById(boleta.getDetallePlanilla().getIdDetalle()).orElse(null)
                            : null;

                    if (detalle == null) {
                        continue;
                    }

                    byte[] pdf = renderPdf(boleta, detalle);

                    String nombreArchivo = detalle.getEmpleado().getNumeroDocumento() + ".pdf";
                    if (!nombresUsados.add(nombreArchivo)) {
                        nombreArchivo = detalle.getEmpleado().getNumeroDocumento() + "-" + boleta.getIdBoleta() + ".pdf";
                        nombresUsados.add(nombreArchivo);
                    }

                    zip.putNextEntry(new ZipEntry(nombreArchivo));
                    zip.write(pdf);
                    zip.closeEntry();
                }
            }

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el ZIP de boletas", e);
        }
    }
}
