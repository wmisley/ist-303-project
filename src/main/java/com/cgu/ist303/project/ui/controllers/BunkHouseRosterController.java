package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.BunkHouseAssignmentDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.model.*;
import com.cgu.ist303.project.dao.sqlite.SqliteCamperRegistrationDAO;
import com.cgu.ist303.project.registrar.BunkHouseAssigner;
import com.cgu.ist303.project.registrar.Registrar;
import com.cgu.ist303.project.registrar.BunHouseRosterGenerator;
import com.cgu.ist303.project.ui.LoggedInUser;
import com.cgu.ist303.project.ui.UIManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class BunkHouseRosterController extends BaseController implements Initializable {
    private static final Logger log = LogManager.getLogger(BunkHouseRosterController.class);

    @FXML
    public TableView<BunkHouseAssignment> bunkhouseTable;
    @FXML
    public ComboBox<BunkHouse> sessions;
    @FXML
    public Button assign;
    @FXML
    public Button back;
    @FXML
    public Button print;
    @FXML
    public Button changeAssignment;

    ObservableList<BunkHouseAssignment> bhas  = null;

    private Registrar registrar = new Registrar();

    public void initialize(URL url, ResourceBundle rb) {
        bunkhouseTable.getColumns().clear();
        bunkhouseTable.setEditable(true);

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BunkHouseAssignment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BunkHouseAssignment, String> param) {
                return new SimpleStringProperty(param.getValue().getCamper().getFirstName());
            }
        });

        TableColumn middleNameCol = new TableColumn("MI");
        middleNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BunkHouseAssignment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BunkHouseAssignment, String> param) {
                return new SimpleStringProperty(param.getValue().getCamper().getMiddleName());
            }
        });

        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BunkHouseAssignment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BunkHouseAssignment, String> param) {
                return new SimpleStringProperty(param.getValue().getCamper().getLastName());
            }
        });

        TableColumn age = new TableColumn("Age");
        age.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BunkHouseAssignment, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<BunkHouseAssignment, Number> param) {
                return new SimpleIntegerProperty(new Integer(param.getValue().getCamper().getAge()));
            }
        });
        age.setStyle("-fx-alignment: CENTER_RIGHT;");

        TableColumn gender = new TableColumn("Gender");
        gender.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BunkHouseAssignment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BunkHouseAssignment, String> param) {
                return new SimpleStringProperty(param.getValue().getCamper().getGenderString());
            }
        });

        TableColumn bunkHouseCol = new TableColumn("Bunk House");
        bunkHouseCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BunkHouseAssignment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BunkHouseAssignment, String> param) {
                return new SimpleStringProperty(param.getValue().getBunkHouse().getBunkHouseName());
            }
        });

        bunkhouseTable.getColumns().addAll(firstNameCol, middleNameCol, lastNameCol, age, gender, bunkHouseCol);

        try {
            registrar.load(2017);
            loadCampSessions();

            int firstBunkId = SqliteCamperRegistrationDAO.NO_SESSIONS;

            if (registrar.getSessions() != null) {
                firstBunkId = registrar.getSessions().get(0).getCampSessioId();
            }

            loadTable();

        } catch (Exception e) {
            displayError(e);
        }

        User u = LoggedInUser.getInstance().getUser();
        changeAssignment.setDisable(u.getType() != User.UserType.Director);
    }

    private void loadTable() {

        try {
            int campSessionId = getCampSessionFromUI().getCampSessioId();

            BunkHouseAssignmentDAO dao = DAOFactory.createBunkHouseAssignmentDAO();
            bhas = dao.query(campSessionId);

            bunkhouseTable.getItems().clear();
            bunkhouseTable.setItems(bhas);
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

    public void changeAssignmentCicked() {
        log.info("Change assginment butoon clicked");

        try {

            BunkHouseAssignment bha = bunkhouseTable.getSelectionModel().getSelectedItem();

            if (bha != null) {
                ObservableList<BunkHouseAssignment> bhas = bunkhouseTable.getItems();
                UIManager.getInstance().showBunkHouseChangeAssignmentScreen(bha, bhas, bunkhouseTable);
            } else {
                displayAlertMessage("Select the camper that you would like to reassign");
            }
        } catch (Exception e) {
            displayError(e);
        }
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

            loadTable();
        });
    }

    public void assignClicked() {
        log.debug("Assign Clicked");

        int campSessionId = getCampSessionFromUI().getCampSessioId();

        BunkHouseAssigner assigner = new BunkHouseAssigner();

        try {
            assigner.assign(2017, campSessionId);

            loadTable();
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void printClicked() {
        log.debug("Print clicked");
        BunHouseRosterGenerator brg = new BunHouseRosterGenerator();

        try{
            if(bhas != null){
                brg.createBunkHouseRosterPdf(bhas, "BunkHouseRoster.pdf");
            }else{
                displayError("No data to print");
            }

            String os = System.getProperty("os.name").toLowerCase();

            if (os.indexOf("mac") > 0) {
                Runtime.getRuntime().exec(new String[]{"open", "-a", "Preview", "BunkHouseRoster.pdf"});
            } else {
                File myFile = new File("BunkHouseRoster.pdf");
                Desktop.getDesktop().open(myFile);
            }
        }catch(Exception e){
            displayError(e);
        }
    }

    public void cancelClicked() throws Exception {
        log.debug("Back clicked");
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }

    public void clearClicked() {
        log.debug("Clear clicked");

        int campSessionId = getCampSessionFromUI().getCampSessioId();

        BunkHouseAssigner bha = new BunkHouseAssigner();

        try {
            bha.clearAssignments(campSessionId);
            loadTable();
        } catch (Exception e) {
            displayError(e);
        }
    }
}
