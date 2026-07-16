package com.todocodeacademy.sistema_planilla.infraestructure.excel;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.PlanillaExcelExporterPort;
import com.todocodeacademy.sistema_planilla.domain.model.DetallePlanilla;
import com.todocodeacademy.sistema_planilla.domain.model.Planilla;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@Component
public class PlanillaExcelExporterAdapter implements PlanillaExcelExporterPort {

    private static final String[] ENCABEZADOS = {
            "Empleado", "Sueldo base", "Asignación familiar", "Bruto", "Descuentos", "Neto"
    };

    @Override
    public byte[] export(Planilla planilla) {

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Planilla " + planilla.getMes() + "-" + planilla.getAnio());

            CellStyle headerStyle = headerStyle(workbook);
            CellStyle moneyStyle = moneyStyle(workbook);
            CellStyle totalStyle = totalStyle(workbook);

            int rowIdx = 0;

            Row headerRow = sheet.createRow(rowIdx++);
            for (int i = 0; i < ENCABEZADOS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(ENCABEZADOS[i]);
                cell.setCellStyle(headerStyle);
            }

            BigDecimal totalSueldoBase = BigDecimal.ZERO;
            BigDecimal totalAsignacion = BigDecimal.ZERO;
            BigDecimal totalBruto = BigDecimal.ZERO;
            BigDecimal totalDescuento = BigDecimal.ZERO;
            BigDecimal totalNeto = BigDecimal.ZERO;

            for (DetallePlanilla detalle : planilla.obtenerDetalles()) {

                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(detalle.getEmpleado().getNombreCompleto());
                setMoneyCell(row.createCell(1), detalle.getSueldoBase(), moneyStyle);
                setMoneyCell(row.createCell(2), detalle.getAsignacionFamiliar(), moneyStyle);
                setMoneyCell(row.createCell(3), detalle.getSueldoBruto(), moneyStyle);
                setMoneyCell(row.createCell(4), detalle.getTotalDescuento(), moneyStyle);
                setMoneyCell(row.createCell(5), detalle.getSueldoNeto(), moneyStyle);

                totalSueldoBase = totalSueldoBase.add(safe(detalle.getSueldoBase()));
                totalAsignacion = totalAsignacion.add(safe(detalle.getAsignacionFamiliar()));
                totalBruto = totalBruto.add(safe(detalle.getSueldoBruto()));
                totalDescuento = totalDescuento.add(safe(detalle.getTotalDescuento()));
                totalNeto = totalNeto.add(safe(detalle.getSueldoNeto()));
            }

            Row totalRow = sheet.createRow(rowIdx);
            Cell totalLabel = totalRow.createCell(0);
            totalLabel.setCellValue("Total");
            totalLabel.setCellStyle(totalStyle);
            setMoneyCell(totalRow.createCell(1), totalSueldoBase, totalStyle);
            setMoneyCell(totalRow.createCell(2), totalAsignacion, totalStyle);
            setMoneyCell(totalRow.createCell(3), totalBruto, totalStyle);
            setMoneyCell(totalRow.createCell(4), totalDescuento, totalStyle);
            setMoneyCell(totalRow.createCell(5), totalNeto, totalStyle);

            for (int i = 0; i < ENCABEZADOS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el Excel de la planilla", e);
        }
    }

    private void setMoneyCell(Cell cell, BigDecimal valor, CellStyle style) {
        cell.setCellValue(safe(valor).doubleValue());
        cell.setCellStyle(style);
    }

    private BigDecimal safe(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }

    private CellStyle headerStyle(XSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    private CellStyle moneyStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        return style;
    }

    private CellStyle totalStyle(XSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        return style;
    }
}
