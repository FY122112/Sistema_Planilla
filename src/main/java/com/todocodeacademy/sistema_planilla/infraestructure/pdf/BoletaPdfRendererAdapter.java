package com.todocodeacademy.sistema_planilla.infraestructure.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.todocodeacademy.sistema_planilla.aplication.command.BoletaPdfData;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.BoletaPdfRendererPort;
import com.todocodeacademy.sistema_planilla.domain.model.Boleta;
import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.domain.model.Empresa;
import com.todocodeacademy.sistema_planilla.domain.model.MovimientoPlanilla;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Component
public class BoletaPdfRendererAdapter implements BoletaPdfRendererPort {

    private static final Font FONT_TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
    private static final Font FONT_SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
    private static final Font FONT_NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 9);
    private static final Font FONT_TABLA_HEADER = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);

    @Override
    public byte[] render(BoletaPdfData data) {

        Boleta boleta = data.boleta();
        DetallePlanilla detalle = data.detalle();
        Empresa empresa = data.empresa();
        Empleado empleado = detalle != null ? detalle.getEmpleado() : null;

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph(
                    empresa != null ? empresa.getRazonSocial() : "TextiLima SAC", FONT_TITULO));
            document.add(new Paragraph(
                    "RUC: " + (empresa != null ? empresa.getRuc() : "-"), FONT_NORMAL));
            document.add(new Paragraph(
                    empresa != null && empresa.getDireccion() != null ? empresa.getDireccion() : "-", FONT_NORMAL));
            document.add(blank());

            Paragraph titulo = new Paragraph("BOLETA DE PAGO — " + periodo(boleta), FONT_SUBTITULO);
            document.add(titulo);
            document.add(new Paragraph("Emitida: " + boleta.getFechaEmision(), FONT_NORMAL));
            document.add(blank());

            document.add(datosEmpleado(empleado));
            document.add(blank());

            document.add(tablaMovimientos(detalle != null ? detalle.obtenerMovimientos() : List.of()));
            document.add(blank());

            document.add(totales(boleta, detalle));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el PDF de la boleta", e);
        }
    }

    private Paragraph blank() {
        return new Paragraph(" ", FONT_NORMAL);
    }

    private String periodo(Boleta boleta) {
        String mes = Month.of(boleta.getPeriodoMes())
                .getDisplayName(TextStyle.FULL, new Locale("es"));
        return capitalize(mes) + " " + boleta.getPeriodoAnio();
    }

    private String capitalize(String texto) {
        return texto.isEmpty() ? texto : Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }

    private PdfPTable datosEmpleado(Empleado empleado) {

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(celda("Trabajador: " + (empleado != null ? empleado.getNombreCompleto() : "-")));
        table.addCell(celda("DNI: " + (empleado != null ? empleado.getNumeroDocumento() : "-")));

        table.addCell(celda("Cargo: " + cargoDe(empleado)));
        table.addCell(celda("Banco / Cuenta: " + bancoDe(empleado)));

        table.addCell(celda("Sistema de pensión: " + pensionDe(empleado)));
        table.addCell(celda(""));

        return table;
    }

    private String cargoDe(Empleado empleado) {
        return empleado != null && empleado.getPuesto() != null ? empleado.getPuesto().getNombre() : "-";
    }

    private String bancoDe(Empleado empleado) {
        if (empleado == null || empleado.getBanco() == null) {
            return "-";
        }
        String cuenta = empleado.getNumeroCuentaBanco() != null ? empleado.getNumeroCuentaBanco() : "-";
        return empleado.getBanco().getNombreBanco() + " / " + cuenta;
    }

    private String pensionDe(Empleado empleado) {
        return empleado != null && empleado.getSistemaPension() != null
                ? empleado.getSistemaPension().getNombre()
                : "-";
    }

    private PdfPCell celda(String texto) {
        PdfPCell cell = new PdfPCell(new Paragraph(texto, FONT_NORMAL));
        cell.setBorder(0);
        cell.setPadding(3);
        return cell;
    }

    private PdfPTable tablaMovimientos(List<MovimientoPlanilla> movimientos) {

        PdfPTable table = new PdfPTable(new float[]{1, 3, 2});
        table.setWidthPercentage(100);

        table.addCell(header("Tipo"));
        table.addCell(header("Concepto"));
        table.addCell(header("Monto"));

        for (MovimientoPlanilla mov : movimientos) {
            table.addCell(celda(mov.getConcepto().getTipoConcepto().getDisplayName()));
            table.addCell(celda(mov.getConcepto().getNombreConcepto()));

            PdfPCell montoCell = new PdfPCell(new Paragraph(formatMoneda(mov.getMonto()), FONT_NORMAL));
            montoCell.setBorder(0);
            montoCell.setPadding(3);
            montoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(montoCell);
        }

        if (movimientos.isEmpty()) {
            table.addCell(celda("-"));
            table.addCell(celda("Sin conceptos adicionales"));
            table.addCell(celda(""));
        }

        return table;
    }

    private PdfPCell header(String texto) {
        PdfPCell cell = new PdfPCell(new Paragraph(texto, FONT_TABLA_HEADER));
        cell.setPadding(4);
        return cell;
    }

    private PdfPTable totales(Boleta boleta, DetallePlanilla detalle) {

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(60);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(celda("Sueldo bruto"));
        table.addCell(montoDerecha(boleta.getSueldoBruto()));

        table.addCell(celda("Total descuentos"));
        table.addCell(montoDerecha(boleta.getTotalDescuento()));

        table.addCell(celda("Aportes del empleador"));
        table.addCell(montoDerecha(detalle != null ? detalle.getTotalAportesEmpleador() : BigDecimal.ZERO));

        PdfPCell netoLabel = new PdfPCell(new Paragraph("NETO A PAGAR", FONT_SUBTITULO));
        netoLabel.setBorder(0);
        netoLabel.setPadding(4);
        table.addCell(netoLabel);
        table.addCell(montoDerecha(boleta.getSueldoNeto(), FONT_SUBTITULO));

        return table;
    }

    private PdfPCell montoDerecha(BigDecimal monto) {
        return montoDerecha(monto, FONT_NORMAL);
    }

    private PdfPCell montoDerecha(BigDecimal monto, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(formatMoneda(monto), font));
        cell.setBorder(0);
        cell.setPadding(4);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private String formatMoneda(BigDecimal monto) {
        BigDecimal valor = monto != null ? monto : BigDecimal.ZERO;
        return "S/ " + valor.setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
