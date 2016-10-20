package com.cgu.ist303.project.registrar;

import com.cgu.ist303.project.ui.controllers.VerifyCheckinItemsController;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;

public class MissingItemListGenerator {
    private static final Logger log = LogManager.getLogger(MissingItemListGenerator.class);

    public MissingItemListGenerator() {
    }

    public void generateList(String filename,
                             ObservableList<VerifyCheckinItemsController.EquipmentItem> eiList,
                             ObservableList<VerifyCheckinItemsController.ArrivalPacketItemItem> apiiList)
    throws Exception {

       String text = "Hi Camper, \n\nYou can not be checked-in to camp until you report to the registration clear " +
               "with the following items:\n\n";

        if (apiiList.size() > 0) {
            if (eiList.size() > 0) {
                text += "\n";
            }

            text += "Arrival Packet Items:\n";
            for (VerifyCheckinItemsController.ArrivalPacketItemItem api : apiiList) {
                text += " - " + api.getDescriptionString() + "\n";
            }

            text += "\n\nEquipment can be purchases or rented at the camp store.";
        }

        if (eiList.size() > 0) {
            text += "Equipment:\n";
            for (VerifyCheckinItemsController.EquipmentItem ei : eiList) {
                text += " - " + ei.getDescriptionText() + "\n";
            }
        }


        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        document.add(new Paragraph(text));

        document.close();
    }
}
