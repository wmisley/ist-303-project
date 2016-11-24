package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.BunkHouseDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.model.BunkHouse;
import com.cgu.ist303.project.dao.model.BunkHouseAssignment;
import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.ui.UIManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BunkHouseSwapController extends BaseController implements Initializable {
    private static final Logger log = LogManager.getLogger(BunkHouseSwapController.class);

    @FXML
    public Button swap;
    @FXML
    public Button cancel;
    @FXML
    public ComboBox<Camper> campers;
    @FXML
    public ComboBox<BunkHouse> bunkHouses;

    private BunkHouseAssignment bha = null;
    private ObservableList<BunkHouseAssignment> bhas = null;

    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setSelectedAssignment(BunkHouseAssignment bbha, ObservableList<BunkHouseAssignment> bbhas) {
        bha = bbha;
        bhas = bbhas;

        loadBunkHouses();
        loadCampers();
    }

    private void loadCampers() {
        BunkHouse selectedBunkHouse = bunkHouses.getSelectionModel().getSelectedItem();

        if (selectedBunkHouse != null) {
            ObservableList<Camper> filteredAssignments = FXCollections.observableList(
                bhas.stream()
                        .filter(assignment -> assignment.getBunkHouse().getBunkHouseId() == selectedBunkHouse.getBunkHouseId())
                        .map(assignment -> assignment.getCamper())
                        .collect(Collectors.toList()));

            campers.getItems().clear();
            campers.setItems(filteredAssignments);

            if (campers.getItems() != null) {
                if (campers.getItems().size() > 0) {
                    campers.getSelectionModel().select(0);
                }
            }
        }
    }

    private void loadBunkHouses() {
        int sessionId = bha.getBunkHouse().getCampSessionId();

        BunkHouseDAO bunkHouseDAO = DAOFactory.createBunkHouseDAO();

        try {
            ObservableList<BunkHouse> allBhs = bunkHouseDAO.query(sessionId);
            ObservableList<BunkHouse> otherBhs = FXCollections.observableList(
                    allBhs.stream()
                            .filter(bh -> bh.getBunkHouseId() != bha.getBunkHouse().getBunkHouseId())
                            .collect(Collectors.toList()));


            bunkHouses.getItems().clear();
            bunkHouses.setItems(otherBhs);

            if (bunkHouses.getItems() != null) {
                if (bunkHouses.getItems().size() > 0) {
                    bunkHouses.getSelectionModel().select(0);
                }
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void bunkHousesChanged() {
        log.debug("bunk house changed");

        loadCampers();
    }

    public void cancelClicked() {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }

    public void swapClicked() {
        log.debug("Swap clicked");
    }
}
