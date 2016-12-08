package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.BunkHouseDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.TribeAssignmentDAO;
import com.cgu.ist303.project.dao.TribeDAO;
import com.cgu.ist303.project.dao.model.*;
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
    private TableView<TribeAssignment> tribeRostertable = null;

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

    public void setTribeAssignment(TribeAssignment assignment, ObservableList<TribeAssignment> tal,
                                   TableView<TribeAssignment> trt) {
        ta = assignment;
        taList = tal;
        tribeRostertable = trt;
        loatTribes();
        loadCampers();
    }

    public void initialize(URL location, ResourceBundle resources) {
    }

    public void swapClicked() {
        try {
            Tribe tribe = tribes.getSelectionModel().getSelectedItem();
            Camper camper = campers.getSelectionModel().getSelectedItem();

            if (tribe == null) {
                displayNotice("Please select a tribe.");
            } else if (camper == null) {
                displayNotice("Please select a camper.");
            } else {
                TribeAssignmentDAO dao = DAOFactory.createTribeAssignmentDAO();
                dao.swap(ta.getCamper().getCamperId(), ta.getTribe().getTribeId(),
                        camper.getCamperId(), tribe.getTribeId());
                UIManager.getInstance().closeCurrentScreenShowPrevious();

                swapAssignmentInList(ta.getCamper().getCamperId(), ta.getTribe(),
                        camper.getCamperId(), tribe);
                tribeRostertable.refresh();
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void swapAssignmentInList(int cId1, Tribe t1, int cId2, Tribe t2) {
        TribeAssignment ass1 = null;
        TribeAssignment ass2 = null;

        for (TribeAssignment assign : taList) {
            if (assign.getCamper().getCamperId() == cId1) {
                ass1 = assign;
            }

            if (assign.getCamper().getCamperId() == cId2) {
                ass2 = assign;
            }
        }

        if (ass1 != null) {
            ass1.setTribe(t2);
        }

        if (ass2 != null) {
            ass2.setTribe(t1);
        }
    }

    public void tribeChanged() {
        loadCampers();
    }

    public void cancelClicked() {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
