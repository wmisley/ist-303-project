package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.BunkHouseDAO;
import com.cgu.ist303.project.dao.CamperRegistrationDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.model.*;
import com.cgu.ist303.project.dao.sqlite.SqliteCamperRegistrationDAO;
import com.cgu.ist303.project.registrar.BunkHouseAssigner;
import com.cgu.ist303.project.registrar.Registrar;
import com.cgu.ist303.project.ui.UIManager;
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
import java.util.ResourceBundle;


public class BunkHouseController implements Initializable {
    private static final Logger log = LogManager.getLogger(BunkHouseController.class);

    @FXML
    public TableView<BunkHouseController> bunkhouseTable;
    @FXML
    public ComboBox<BunkHouse> sessions;
    @FXML
    public Button assign;
    @FXML
    public Button back;
    @FXML
    public Button print;

    private Registrar registrar = new Registrar();

    public void initialize(URL url, ResourceBundle rb) {
        bunkhouseTable.getColumns().clear();
        bunkhouseTable.setEditable(true);

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Camper, String>("firstName"));
        TableColumn middleNameCol = new TableColumn("MI");
        middleNameCol.setCellValueFactory(new PropertyValueFactory<Camper, String>("middleInitial"));
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Camper, String>("lastName"));

        TableColumn age = new TableColumn("Age");
        age.setCellValueFactory(new PropertyValueFactory<Camper, String>("age"));
        age.setStyle("-fx-alignment: CENTER_RIGHT;");

        TableColumn gender = new TableColumn("Gender");
        gender.setCellValueFactory(new PropertyValueFactory<Camper, String>("gender"));

        TableColumn bunkHouseCol = new TableColumn("Bunk House");
        bunkHouseCol.setCellValueFactory(new PropertyValueFactory<Camper, String>("bunkHouseName"));

        bunkhouseTable.getColumns().addAll(firstNameCol, middleNameCol, lastNameCol, age, gender, bunkHouseCol);

        try {
            registrar.load(2017);
            loadCampSessions();

            int firstBunkId = SqliteCamperRegistrationDAO.NO_SESSIONS;

            if (registrar.getSessions() != null) {
                firstBunkId = registrar.getSessions().get(0).getCampSessioId();
            }

            loadTable(firstBunkId);

        } catch (Exception e) {
            displayError(e);
        }
    }

    private void loadTable(int bunkId) {
        ObservableList<BunkHouseAssignment> obsList  = null;

        try {
            //TODO: Query bunk house assignments and display them here
        } catch (Exception e) {
            displayError(e);
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

    public void displayNotice(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setHeaderText("");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void displayError(Exception e) {
        e.printStackTrace();
        log.error(e);
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setContentText(e.getMessage());
        errorAlert.showAndWait();
    }

    public void assignClicked() {
        log.debug("Assign Clicked");

        int campSessionId = getCampSessionFromUI().getCampSessioId();

        BunkHouseAssigner assigner = new BunkHouseAssigner();

        try {
            assigner.assign(2017, campSessionId);
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void printClicked() {
        log.debug("Print clicked");
    }

    public void cancelClicked() throws Exception {
        log.debug("Back clicked");
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
