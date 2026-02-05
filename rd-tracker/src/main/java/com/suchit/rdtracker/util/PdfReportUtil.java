package com.suchit.rdtracker.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.suchit.rdtracker.dto.PaidCustomerDTO;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.YearMonth;
import java.util.List;

public class PdfReportUtil {

    public static byte[] generatePaidCustomersPdf(
            YearMonth month,
            List<PaidCustomerDTO> customers
    ) throws Exception {

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        // Title
        Paragraph title = new Paragraph("RD COLLECTION REPORT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("Month: " + month));
        document.add(new Paragraph("Generated On: " + java.time.LocalDate.now()));
        document.add(Chunk.NEWLINE);

        // ✅ Table (ONLY 3 columns now)
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 6, 3});

        addHeader(table, "Account No", headerFont);
        addHeader(table, "Name", headerFont);
        addHeader(table, "Amount", headerFont);

        double total = 0;

        for (PaidCustomerDTO c : customers) {
            table.addCell(new PdfPCell(
                    new Phrase(String.valueOf(c.getAccountNo()), bodyFont)));
            table.addCell(new PdfPCell(
                    new Phrase(c.getName(), bodyFont)));
            table.addCell(new PdfPCell(
                    new Phrase("₹" + c.getAmountPaid(), bodyFont)));

            total += c.getAmountPaid();
        }

        document.add(table);
        document.add(Chunk.NEWLINE);

        Paragraph totalPara = new Paragraph(
                "Total Collected: ₹" + total,
                headerFont
        );
        totalPara.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalPara);

        document.close();
        return out.toByteArray();
    }

    private static void addHeader(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new Color(230, 230, 230));
        table.addCell(cell);
    }
}
