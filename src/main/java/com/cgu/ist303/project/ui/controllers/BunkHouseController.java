package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.BunkHouseDAO;
import com.cgu.ist303.project.dao.CamperRegistrationDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.model.*;
import com.cgu.ist303.project.dao.sqlite.SqliteCamperRegistrationDAO;
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
    private TableView<BunkHouseController> bunkhouseTable;
    @FXML
    private ComboBox<BunkHouse> sessions;
    @FXML
    private Button makePayment;
    @FXML
    private Button emergencyContacts;

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
        TableColumn phoneNumber = new TableColumn("Phone Number");
        phoneNumber.setCellValueFactory(new PropertyValueFactory<Camper, String>("phoneNumberString"));
        TableColumn age = new TableColumn("Age");
        age.setCellValueFactory(new PropertyValueFactory<Camper, String>("age"));
        age.setStyle("-fx-alignment: CENTER_RIGHT;");
        TableColumn gender = new TableColumn("Gender");
        gender.setCellValueFactory(new PropertyValueFactory<Camper, String>("gender"));
        TableColumn payment = new TableColumn("Amount Due");
        payment.setCellValueFactory(new PropertyValueFactory<Camper, String>("formattedAmountDue"));
        payment.setStyle("-fx-alignment: CENTER_RIGHT;");

        bunkhouseTable.getColumns().addAll(firstNameCol, middleNameCol, lastNameCol, phoneNumber, age, gender, payment);

        try {
            registrar.load(2017);
            loadCampSessions();

            int firstBunkId = SqliteCamperRegistrationDAO.NO_SESSIONS;

            if (registrar.getSessions() != null) {
                firstBunkId = registrar.getSessions().get(0).getCampSessioId();
            }

            loadTable(firstBunkId);

        } catch (Exception e) {
            log.error(e);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }

        /*
        campersTable.setRowFactory(tv -> {
            TableRow<CamperRegistration> row = new TableRow<>();

             row.disableProperty().bind(
                      Bindings.selectInteger(row.itemProperty(), "value")
                     .lessThan(5));
            return row;

        });
        */
    }

    public void emergencyContactsClicked() throws Exception {
        UIManager.getInstance().showEmergencyContactsScreen();
    }

    private void loadTable(int bunkId) {
        BunkHouseDAO bunkDAO = DAOFactory.createBunkHouseDAO();
        ObservableList<BunkHouseAssignment> obsList  = null;

        try {

        //    obsList = bunkDAO.queryBunkhouses(2017, bunkId);
         //   bunkhouseTable.getItems().clear();
         //   bunkhouseTable.setItems(obsList);
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

    public void displayNotice(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setHeaderText("");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void printClicked() {
    }

    public void cancelClicked() throws Exception {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
