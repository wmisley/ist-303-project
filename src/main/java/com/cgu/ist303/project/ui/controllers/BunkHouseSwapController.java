package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.BunkHouseAssignmentDAO;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
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
    @FXML
    public Label camperLabel;

    private BunkHouseAssignment bha = null;
    private ObservableList<BunkHouseAssignment> bhas;
    public TableView<BunkHouseAssignment> bunkhouseTable = null;

    private ObservableList<Camper> filteredCampers;
    private ObservableList<BunkHouse> otherBhs;

    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setSelectedAssignment(BunkHouseAssignment bbha, ObservableList<BunkHouseAssignment> bbhas,
                                      TableView<BunkHouseAssignment> table ) {
        bha = bbha;
        bhas = FXCollections.observableList(new ArrayList<>(bbhas));
        bunkhouseTable = table;

        loadBunkHouses();
        loadCampers();
    }

    private void loadCampers() {
        BunkHouse selectedBunkHouse = bunkHouses.getSelectionModel().getSelectedItem();

        if (selectedBunkHouse != null) {
            filteredCampers = FXCollections.observableList(
                bhas.stream()
                        .filter(assignment -> assignment.getBunkHouse().getBunkHouseId() == selectedBunkHouse.getBunkHouseId())
                        .map(assignment -> assignment.getCamper())
                        .collect(Collectors.toList()));

            campers.getItems().clear();
            campers.setItems(filteredCampers);

            if (campers.getItems().size() > 0) {
                if (campers.getItems().size() > 0) {
                    campers.getSelectionModel().select(0);
                }

                campers.setDisable(false);
                camperLabel.setDisable(false);
            } else {
                campers.setDisable(true);
                camperLabel.setDisable(true);
            }
        }
    }

    private void loadBunkHouses() {
        int sessionId = bha.getBunkHouse().getCampSessionId();

        BunkHouseDAO bunkHouseDAO = DAOFactory.createBunkHouseDAO();

        try {
            ObservableList<BunkHouse> allBhs = bunkHouseDAO.query(sessionId);
            otherBhs = FXCollections.observableList(
                    allBhs.stream()
                            .filter(bh -> (bh.getBunkHouseId() != bha.getBunkHouse().getBunkHouseId() &&
                                    (bha.getBunkHouse().getGender() == bh.getGender())))
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

    private void swapAssignmentInList(int cId1, BunkHouse bh1, int cId2, BunkHouse bh2) {
        BunkHouseAssignment ass1 = null;
        BunkHouseAssignment ass2 = null;

        for (BunkHouseAssignment assign : bhas) {
            if (assign.getCamper().getCamperId() == cId1) {
                ass1 = assign;
            }

            if (assign.getCamper().getCamperId() == cId2) {
                ass2 = assign;
            }
        }

        if (ass1 != null) {
            ass1.setBunkHouse(bh2);
        }

        if (ass2 != null) {
            ass2.setBunkHouse(bh1);
        }

        bunkhouseTable.refresh();
    }

    public void swapClicked() {
        log.debug("Swap clicked");

        BunkHouse bh = bunkHouses.getSelectionModel().getSelectedItem();
        Camper c = campers.getSelectionModel().getSelectedItem();

        try {
            if (bh != null) {
                BunkHouseAssignmentDAO dao = DAOFactory.createBunkHouseAssignmentDAO();
                int swapCamperId = -1;

                if (c != null) {
                    swapCamperId = c.getCamperId();
                }

                dao.swap(bha.getCamper().getCamperId(), bha.getBunkHouse().getBunkHouseId(),
                        swapCamperId, bh.getBunkHouseId());

                UIManager.getInstance().closeCurrentScreenShowPrevious();
                swapAssignmentInList(bha.getCamper().getCamperId(), bha.getBunkHouse(), swapCamperId, bh);
            } else {
                displayNotice("Please select a bunk house.");
            }
        } catch (Exception e) {
            displayError(e);
        }
    }
}
