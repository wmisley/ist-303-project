package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.model.ArrivalPacketItem;
import com.cgu.ist303.project.dao.model.Equipment;
import com.cgu.ist303.project.registrar.MissingItemListGenerator;
import com.cgu.ist303.project.ui.UIManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.CheckListView;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class VerifyCheckinItemsController extends BaseController implements Initializable {
    private static final Logger log = LogManager.getLogger(VerifyCheckinItemsController.class);

    @FXML
    private CheckListView<EquipmentItem> equipment;
    @FXML
    private CheckListView<ArrivalPacketItemItem> packet;
    @FXML
    private Button ok;
    @FXML
    private Button cancel;

    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<EquipmentItem> equipmentList = FXCollections.observableArrayList();
        equipmentList.add(new EquipmentItem("Backpack"));
        equipmentList.add(new EquipmentItem("Boots"));
        equipmentList.add(new EquipmentItem("Bug Spray"));
        equipmentList.add(new EquipmentItem("Compass or GPS"));
        equipmentList.add(new EquipmentItem("Deodorant"));
        equipmentList.add(new EquipmentItem("First aid kit"));
        equipmentList.add(new EquipmentItem("Riding Helmet"));
        equipmentList.add(new EquipmentItem("Shorts (13 pair)"));
        equipmentList.add(new EquipmentItem("Shirts (13)"));
        equipmentList.add(new EquipmentItem("Sleeping Bag"));
        equipmentList.add(new EquipmentItem("Socks (13 pair)"));
        equipmentList.add(new EquipmentItem("Sun Screen"));
        equipmentList.add(new EquipmentItem("Toothbrush"));
        equipmentList.add(new EquipmentItem("Towel"));
        equipmentList.add(new EquipmentItem("Underwear (13 pair)"));
        equipmentList.add(new EquipmentItem("Water Bottle"));
        equipmentList.add(new EquipmentItem("Whistle"));
        equipment.setItems(equipmentList);

        ObservableList<ArrivalPacketItemItem> packetItemsList = FXCollections.observableArrayList();
        packetItemsList.add(new ArrivalPacketItemItem("Medical Forms"));
        packetItemsList.add(new ArrivalPacketItemItem("Legal Forms"));
        packetItemsList.add(new ArrivalPacketItemItem("Emergency Contact Information"));
        packet.setItems(packetItemsList);
    }

    private ObservableList<EquipmentItem> getUncheckedEquipment() {
        ObservableList<EquipmentItem> checkedEquipmentList = FXCollections.observableArrayList();
        checkedEquipmentList.addAll(equipment.getCheckModel().getCheckedItems());

        ObservableList<EquipmentItem> uncheckedEquipmentList = FXCollections.observableArrayList();
        uncheckedEquipmentList.addAll(equipment.getItems());

        uncheckedEquipmentList.removeAll(checkedEquipmentList);

        return uncheckedEquipmentList;
    }

    private ObservableList<ArrivalPacketItemItem> getUncheckedArrivalPacketItems() {
        ObservableList<ArrivalPacketItemItem> checkedArrivalPacketItems = FXCollections.observableArrayList();
        checkedArrivalPacketItems.addAll(packet.getCheckModel().getCheckedItems());

        ObservableList<ArrivalPacketItemItem> uncheckedArrivalPacketItems = FXCollections.observableArrayList();
        uncheckedArrivalPacketItems.addAll(packet.getItems());

        uncheckedArrivalPacketItems.removeAll(checkedArrivalPacketItems);

        return uncheckedArrivalPacketItems;
    }
    public void okClicked() {
        log.debug("Ok clicked");
        ObservableList<EquipmentItem> uncheckedEquipmentList = getUncheckedEquipment();
        ObservableList<ArrivalPacketItemItem> uncheckedArrivalPacketItems = getUncheckedArrivalPacketItems();

        log.info("Equipment user does not have: {}", uncheckedEquipmentList);
        log.info("Arrival packet items user does not have: {}", uncheckedArrivalPacketItems);

        if ((uncheckedEquipmentList.size() == 0) && (uncheckedArrivalPacketItems.size() == 0)) {
            this.displayAlertMessage("Camper is now checked in.");
            UIManager.getInstance().closeCurrentScreenShowPrevious();
        } else {
            promptItemsNotChecked(uncheckedEquipmentList, uncheckedArrivalPacketItems);
        }
    }

    public void cancelClicked() throws Exception {
        log.debug("Cancel clicked");
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }

    private void promptItemsNotChecked(ObservableList<EquipmentItem> uncheckedEquipment,
                                       ObservableList<ArrivalPacketItemItem> uncheckedPacketItems) {
        String message = "The camper can not be checked in since the following items are missing from the checklist: \n\n";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText("");

        for (ArrivalPacketItemItem api : uncheckedPacketItems) {
            message += " - " + api.getDescriptionString() + "\n";
        }

        for (EquipmentItem ei : uncheckedEquipment) {
            message += " - " + ei.getDescriptionText() + "\n";
        }

        message += "\nWould you like to print the list for the camper?";

        alert.setContentText(message);
        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonYes) {
            try {
                MissingItemListGenerator mlg = new MissingItemListGenerator();
                mlg.generateList("missing.pdf", uncheckedEquipment, uncheckedPacketItems);
                Runtime.getRuntime().exec(new String[]{"open", "-a", "Preview", "missing.pdf"});

                cancelClicked();
            } catch (Exception e) {
                log.error("Could not generate missing equipment / packet item file");
                displayErrorMessage("Could not generate missing equipment / packet item file", e);
            }
        }
    }

    public static class ArrivalPacketItemItem extends ArrivalPacketItem {
        private ReadOnlyStringWrapper name = new ReadOnlyStringWrapper();
        private BooleanProperty selected = new SimpleBooleanProperty(false);

        public ArrivalPacketItemItem(String s) {
            this.setDescriptionString(s);
            name.set(s);
        }

        public ReadOnlyStringProperty nameProperty() {
            return name.getReadOnlyProperty();
        }

        public BooleanProperty selectedProperty() {
            return selected;
        }

        public boolean isSelected() {
            return selected.get();
        }

        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }

        public String toString() {
            return getDescriptionString();
        }
    }

    public static class EquipmentItem extends Equipment {
        private ReadOnlyStringWrapper name = new ReadOnlyStringWrapper();
        private BooleanProperty selected = new SimpleBooleanProperty(false);

        public EquipmentItem(String s) {
            setDescriptionText(s);
            name.set(s);
        }

        public ReadOnlyStringProperty nameProperty() {
            return name.getReadOnlyProperty();
        }

        public BooleanProperty selectedProperty() {
            return selected;
        }

        public boolean isSelected() {
            return selected.get();
        }

        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }

        public String toString() {
            return getDescriptionText();
        }
    }
}
