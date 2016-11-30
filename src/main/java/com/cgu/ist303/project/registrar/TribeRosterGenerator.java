package com.cgu.ist303.project.registrar;

import com.cgu.ist303.project.dao.model.TribeAssignment;
import com.itextpdf.text.*;
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
        Paragraph campHead = new Paragraph("Gila Breadth Camp");
        campHead.setAlignment(Element.ALIGN_CENTER);
        document.add(campHead);

        document.add(new Paragraph("Tribe Roster"));

        PdfPTable rosterTable = new PdfPTable(4);
        rosterTable.setSpacingBefore(10);
        rosterTable.setWidthPercentage((float) 100);

        rosterTable.addCell(getBorderCell("First Name"));
        rosterTable.addCell(getBorderCell("Last Name"));
        rosterTable.addCell(getBorderCell("Age"));
        rosterTable.addCell(getBorderCell("Tribe Name"));
        rosterTable.completeRow();

        for (TribeAssignment assignment : list) {
            rosterTable.addCell(getBorderCell(assignment.getCamper().getFirstName()));
            rosterTable.addCell(getBorderCell(assignment.getCamper().getLastName()));
            rosterTable.addCell(getBorderCell(Integer.toString(assignment.getCamper().getAge())));
            rosterTable.addCell(getBorderCell(assignment.getTribe().getTribeName()));
            rosterTable.completeRow();
        }

        document.add(rosterTable);
        document.close();
    }

    private PdfPCell getBorderCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
//        cell.setBorder(1);
//        cell.setBorder(2);
//        cell.setBorder(3);
//        cell.setBorder(4);
        return  cell;
    }
}
