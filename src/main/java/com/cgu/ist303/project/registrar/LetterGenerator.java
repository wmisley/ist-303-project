package com.cgu.ist303.project.registrar;

import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.RejectedApplication;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by will4769 on 10/16/16.
 */
public class LetterGenerator {
    private static final Logger log = LogManager.getLogger(LetterGenerator.class);

    public LetterGenerator() {
    }

    private String getReturnAddressText() {
        String address = "Gila Breath Camp\n";
        address += "1234 Gila Monster Dr.\n";
        address += "Big Bear, CA 98877\n";

        return address;
    }

    private String getSendAddressText(Camper c, String prefix) {
        String address = String.format("%s%s %s\n", prefix, c.getRpFirstName(), c.getRpLastName());
        address += String.format("%s%s\n", prefix, c.getStreet());
        address += String.format("%s%s, %s %s\n", prefix, c.getCity(), c.getState(), c.getZipCode());

        return address;
    }

    public void createRejectionLetter(String filePath, Camper c, CampSession session,
                                      RejectedApplication.RejectionReason reason) throws Exception {
        String letter = "";

        letter += getReturnAddressText();
        letter += "\n";
        letter += String.format("%s %s\n", c.getRpFirstName(), c.getRpLastName());
        letter += String.format("%s\n", c.getStreet());
        letter += String.format("%s, %s, %s\n", c.getCity(), c.getState(), c.getZipCode());
        letter += "\n";
        letter += String.format("Dear %s %s,\n", c.getRpFirstName(), c.getRpLastName());
        letter += "\n";

        if (session != null) {
            letter += String.format("We regret to inform you that your application to the camp session starting on %d/%d/%d ",
                    session.getStartMonth(), session.getStartDay(), session.getCampYear());
            letter += String.format("and ending on %d/%d/%d, has been rejected for the following reason:\n\n",
                    session.getEndMonth(), session.getEndDay(), session.getCampYear());
        } else {
            letter += String.format("We regret to inform you that your application to the camp has been rejected for the following reason:\n\n");
        }

        if (reason == RejectedApplication.RejectionReason.AlreadyRegisterForYear) {
            letter += String.format("Application %s %s is already registered for a camp session in the same year.\n",
                c.getFirstName(), c.getLastName());
        } else if (reason == RejectedApplication.RejectionReason.NotReceivedDuringAllowableTimeframe) {
            letter += "The application was not received in the allowable timeframe.\n";
        } else if (reason == RejectedApplication.RejectionReason.GenderLimitReached) {
            letter += "The gender limit has been reached for the session.\n";
        } else if (reason == RejectedApplication.RejectionReason.ApplicationIncomplete) {
            letter += "The application was not completed.\n";
        } else if (reason == RejectedApplication.RejectionReason.CamperNotInAgeRange) {
            letter += "The applicant camper is outside the allowable age range.\n";
        } else {
            log.error("Generating rejection letter without a valid rejection reason");
        }

        letter += "\n";
        letter += "We are sorry for this inconvenience and hope you apply again to Gila Camp at some time in the future.\n";
        letter += "\n";
        letter += "\n";
        letter += "\n";
        letter += "\n";
        letter += "Bernard Trumble\n";
        letter += "Camp Director\n";

        log.debug("Generating rejection letter:");
        log.debug(letter);

        createLetterPDFFile(filePath, letter, c);
        createLetterPDFEnvelope("envelop.pdf", c);
    }

    public void createLetterPDFFile(String filename, String text, Camper c) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        document.add(new Paragraph(text));

        Double width = 72 * 9.5;
        Double height = 72 *4.125;
        Rectangle envelope = new Rectangle(width.intValue(), height.intValue());
        document.setPageSize(envelope);
        document.setMargins(25f, 25f, 25f, 0f);
        document.newPage();

        PdfPTable table = new PdfPTable(2);
        table.setSpacingBefore(0);
        table.setWidthPercentage((float) 100);
        table.addCell(getWhiteBorderCell(getReturnAddressText() + "\n\n\n\n\n\n\n\n"));
        table.addCell(getWhiteBorderCell(""));
        table.addCell(getWhiteBorderCell(""));
        table.addCell(getWhiteBorderCell(getSendAddressText(c,"")));
        document.add(table);

        document.close();
    }

    private PdfPCell getWhiteBorderCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBorder(0);
        return cell;
    }

    public void createLetterPDFEnvelope(String filename, Camper c) throws Exception {
        Double width = 72 * 9.5;
        Double height = 72 *4.125;
        Rectangle envelope = new Rectangle(width.intValue(), height.intValue());
        Document document = new Document(envelope, 25f, 25f, 25f, 0f);
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();

        PdfPTable table = new PdfPTable(2);
        table.setSpacingBefore(0);
        table.setWidthPercentage((float) 100);
        table.addCell(getWhiteBorderCell(getReturnAddressText() + "\n\n\n\n\n\n\n\n"));
        table.addCell(getWhiteBorderCell(""));
        table.addCell(getWhiteBorderCell(""));
        table.addCell(getWhiteBorderCell(getSendAddressText(c,"")));
        document.add(table);

        document.close();
    }
}
