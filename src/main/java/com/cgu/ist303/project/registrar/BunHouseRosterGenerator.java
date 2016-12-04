package com.cgu.ist303.project.registrar;

import com.cgu.ist303.project.dao.model.BunkHouseAssignment;
import com.cgu.ist303.project.dao.model.TribeAssignment;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by 33843 on 12/4/2016.
 */
public class BunHouseRosterGenerator {

    private final Font bold14 = new Font(Font.FontFamily.COURIER,14,Font.BOLD);
    private final Font bold12 = new Font(Font.FontFamily.COURIER,12,Font.NORMAL);
    private final Font normal12 = new Font(Font.FontFamily.COURIER,12,Font.NORMAL);

    public void createBunkHouseRosterPdf(ObservableList<BunkHouseAssignment> bhas, String fileName) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        Paragraph campHead = new Paragraph("Gila Breadth Camp",bold14);
        campHead.setAlignment(Element.ALIGN_CENTER);
        document.add(campHead);

        document.add(new Paragraph("Bunk House Roster",bold12));

        PdfPTable rosterTable = new PdfPTable(4);
        rosterTable.setSpacingBefore(10);
        rosterTable.setWidthPercentage((float) 100);

        rosterTable.addCell(getBorderCellWithBoldText("First Name"));
        rosterTable.addCell(getBorderCellWithBoldText("Last Name"));
        rosterTable.addCell(getBorderCellWithBoldText("Age"));
        rosterTable.addCell(getBorderCellWithBoldText("Bunk House Name"));
        rosterTable.completeRow();

        for (BunkHouseAssignment assignment : bhas) {
            rosterTable.addCell(getBorderCell(assignment.getCamper().getFirstName()));
            rosterTable.addCell(getBorderCell(assignment.getCamper().getLastName()));
            rosterTable.addCell(getBorderCell(Integer.toString(assignment.getCamper().getAge())));
            rosterTable.addCell(getBorderCell(assignment.getBunkHouse().getBunkHouseName()));
            rosterTable.completeRow();
        }

        document.add(rosterTable);
        document.close();
    }

    private PdfPCell getBorderCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text,normal12));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        return  cell;
    }

    private PdfPCell getBorderCellWithBoldText(String text){
        PdfPCell cell = new PdfPCell(new Phrase(text,bold12));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
}
