package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.CamperRegistrationDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.sqlite.SqliteCamperRegistrationDAO;
import com.cgu.ist303.project.registrar.Registrar;
import com.cgu.ist303.project.ui.UIManager;
import com.cgu.ist303.project.ui.model.EquipmentItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class CheckinController implements Initializable {
    private static final Logger log = LogManager.getLogger(CheckinController.class);

    @FXML
    private TableView<Camper> campersTable;
    @FXML
    private ComboBox<CampSession> sessions;

    private Registrar registrar = new Registrar();

    public void initialize(URL url, ResourceBundle rb) {
        campersTable.getColumns().clear();
        campersTable.setEditable(true);

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Camper, String>("firstName"));
        TableColumn middleNameCol = new TableColumn("Middle Name");
        middleNameCol.setCellValueFactory(new PropertyValueFactory<Camper, String>("middleName"));
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Camper, String>("lastName"));
        TableColumn phoneNumber = new TableColumn("Phone Number");
        phoneNumber.setCellValueFactory(new PropertyValueFactory<Camper, String>("phoneNumberString"));
        TableColumn age = new TableColumn("Age");
        age.setCellValueFactory(new PropertyValueFactory<Camper, String>("age"));
        TableColumn gender = new TableColumn("Gender");
        gender.setCellValueFactory(new PropertyValueFactory<Camper, String>("gender"));

        campersTable.getColumns().addAll(firstNameCol, middleNameCol, lastNameCol, phoneNumber, age, gender);

        try {
            registrar.load(2017);
            loadCampSessions();

            int firstSessionId = SqliteCamperRegistrationDAO.NO_SESSIONS;

            if (registrar.getSessions() != null) {
                firstSessionId = registrar.getSessions().get(0).getCampSessioId();
            }

            loadTable(firstSessionId);
        } catch (Exception e) {
            log.error(e);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }

    private void loadTable(int sessionId) {
        CamperRegistrationDAO regDAO = DAOFactory.createCamperRegistrationDAO();
        ObservableList<Camper> obsList  = null;

        try {

            obsList = regDAO.queryRegisteredCampers(2017, sessionId);
            campersTable.getItems().clear();
            campersTable.setItems(obsList);
        } catch (Exception e) {
            log.error(e);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }

    private CampSession getCampSessionFromUI() {
        int index = sessions.getSelectionModel().getSelectedIndex();
        CampSession campSession = null;

        if (index >= 0) {
            campSession = registrar.getSessions().get(index);
        }

        return campSession;
    }

    private void loadCampSessions() {
        List<CampSession> sessionList = registrar.getSessions();
        List<String> list = new ArrayList<String>();

        for (CampSession session : sessionList) {
            list.add(session.getShortDateString());
        }

        ObservableList obList = FXCollections.observableList(list);
        sessions.getItems().clear();
        sessions.setItems(obList);

        if (sessions.getItems() != null) {
            if (sessions.getItems().size() > 0) {
                sessions.getSelectionModel().select(0);
            }
        }

        sessions.setOnAction((event) -> {
            CampSession session = getCampSessionFromUI();
            log.debug("User selected session {}", session.getShortDateString());

            loadTable(session.getCampSessioId());
        });
    }

    public void checkinClicked() throws Exception {
        Camper camper = campersTable.getSelectionModel().getSelectedItem();

        log.debug("Camper {} {} was selected for check-in by the user", camper.getFirstName(),
                camper.getLastName());

        UIManager.getInstance().showVerifyCheckinItems();
    }

    public void cancelClicked() throws Exception {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
