package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.BunkHouseDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.TribeDAO;
import com.cgu.ist303.project.dao.model.BunkHouse;
import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.Tribe;
import com.cgu.ist303.project.dao.model.TribeAssignment;
import com.cgu.ist303.project.ui.UIManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by will4769 on 12/7/16.
 */
public class TribeSwapController extends BaseController implements Initializable {
    private static final Logger log = LogManager.getLogger(TribeSwapController.class);
    private TribeAssignment ta;
    private ObservableList<Tribe> otherTribes = null;
    private ObservableList<Camper> filteredCampers = null;
    private ObservableList<TribeAssignment> taList = null;

    @FXML
    public Button swap;
    @FXML
    public Button cancel;
    @FXML
    public ComboBox<Camper> campers;
    @FXML
    public ComboBox<Tribe> tribes;

    private void loatTribes() {
        int sessionId = ta.getTribe().getCampSessionId();

        TribeDAO tribeDAO = DAOFactory.createTribeDAO();

        try {
            ObservableList<Tribe> allTribes = tribeDAO.query(sessionId);
            otherTribes = FXCollections.observableList(
                    allTribes.stream()
                            .filter(tribe -> (tribe.getTribeId() != ta.getTribe().getTribeId()))
                            .collect(Collectors.toList()));

            tribes.getItems().clear();
            tribes.setItems(otherTribes);

            if (tribes.getItems() != null) {
                if (tribes.getItems().size() > 0) {
                    tribes.getSelectionModel().select(0);
                }
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void loadCampers() {
        Tribe selectedTribe= tribes.getSelectionModel().getSelectedItem();

        if (selectedTribe != null) {
            filteredCampers = FXCollections.observableList(
                    taList.stream()
                            .filter(assignment -> assignment.getTribe().getTribeId() == selectedTribe.getTribeId())
                            .map(assignment -> assignment.getCamper())
                            .collect(Collectors.toList()));

            campers.getItems().clear();
            campers.setItems(filteredCampers);

            if (campers.getItems().size() > 0) {
                if (campers.getItems().size() > 0) {
                    campers.getSelectionModel().select(0);
                }

                campers.setDisable(false);
                //camperLabel.setDisable(false);
            } else {
                campers.setDisable(true);
                //camperLabel.setDisable(true);
            }
        }
    }

    public void setTribeAssignment(TribeAssignment assignment, ObservableList<TribeAssignment> tal) {
        ta = assignment;
        taList = tal;
        loatTribes();
        loadCampers();
    }

    public void initialize(URL location, ResourceBundle resources) {
    }

    public void swapClicked() {

    }

    public void tribeChanged() {
        loadCampers();
    }

    public void cancelClicked() {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
