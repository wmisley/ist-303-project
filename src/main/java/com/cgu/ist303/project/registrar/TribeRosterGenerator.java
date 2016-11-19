package com.cgu.ist303.project.registrar;

import com.cgu.ist303.project.dao.model.TribeAssignment;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;


public class TribeRosterGenerator {
    public void createTribeRosterPdf(List<TribeAssignment> list, String fileName) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        PdfPTable rosterTable = new PdfPTable(3);
        rosterTable.setSpacingBefore(0);
        rosterTable.setWidthPercentage((float) 100);

        for (TribeAssignment assignment : list) {
            rosterTable.addCell(getBorderCell(assignment.getCamper().getFirstName()));
            rosterTable.addCell(getBorderCell(Integer.toString(assignment.getCamper().getAge())));
            rosterTable.addCell(getBorderCell(assignment.getTribe().getTribeName()));
            rosterTable.completeRow();

        }

        document.add(rosterTable);
        document.close();
    }

    private PdfPCell getBorderCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBorder(1);
        return  cell;
    }
}
