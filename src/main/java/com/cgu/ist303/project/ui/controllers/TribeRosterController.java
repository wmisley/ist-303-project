package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.TribeAssignmentDAO;
import com.cgu.ist303.project.dao.model.*;
import com.cgu.ist303.project.dao.sqlite.SqliteCamperRegistrationDAO;
import com.cgu.ist303.project.registrar.Registrar;
import com.cgu.ist303.project.registrar.TribeAssigner;
import com.cgu.ist303.project.registrar.TribeRosterGenerator;
import com.cgu.ist303.project.ui.LoggedInUser;
import com.cgu.ist303.project.ui.UIManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class TribeRosterController extends BaseController implements Initializable {

    private static final Logger log = LogManager.getLogger(TribeRosterController.class);

    @FXML
    private TableView<TribeAssignment> tribeRostertable;
    @FXML
    public ComboBox<CampSession> sessions;
    @FXML
    public Button print;
    @FXML
    public Button back;
    @FXML
    public Button assign;
    @FXML
    public Button clear;

    List<TribeAssignment> list;

    private Registrar registrar = new Registrar();

    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<TribeAssignment, String> campersFNameCol = new TableColumn<TribeAssignment, String>("First Name");
        campersFNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TribeAssignment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TribeAssignment, String> param) {
                return new SimpleStringProperty(param.getValue().getCamper().getFirstName());
            }
        });

        TableColumn<TribeAssignment, String> campersLNameCol = new TableColumn<TribeAssignment, String>("Last Name");
        campersLNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TribeAssignment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TribeAssignment, String> param) {
                return new SimpleStringProperty(param.getValue().getCamper().getLastName());
            }
        });

        TableColumn<TribeAssignment, Number> ageCol = new TableColumn<TribeAssignment, Number>("Age");
        ageCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TribeAssignment, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<TribeAssignment, Number> param) {
                return new SimpleIntegerProperty(param.getValue().getCamper().getAge());
            }
        });
        ageCol.setStyle("-fx-alignment: CENTER_RIGHT;");

        TableColumn gender = new TableColumn("Gender");
        gender.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TribeAssignment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TribeAssignment, String> param) {
                return new SimpleStringProperty(param.getValue().getCamper().getGenderString());
            }
        });
        gender.setStyle("-fx-alignment: CENTER_RIGHT;");

        TableColumn<TribeAssignment, String> assignedTribeCol = new TableColumn<TribeAssignment, String>("Tribe");
        assignedTribeCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TribeAssignment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TribeAssignment, String> param) {
                return new SimpleStringProperty(param.getValue().getTribe().getTribeName());
            }
        });

        tribeRostertable.getColumns().addAll(campersFNameCol, campersLNameCol, ageCol, gender, assignedTribeCol);

        try {
            registrar.load(2017);
            loadCampSession();

        } catch (Exception e) {
            displayError(e);
        }
        print.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                printClicked();
            }
        });

        User u = LoggedInUser.getInstance().getUser();

        //This button on is only active for camp director
        clear.setDisable(u.getType() != User.UserType.Director);
    }

    public void clearSession() {
        log.info("User pressed to clear the session");

        int campSessionId = getCampSessionFromUI().getCampSessioId();

        try {
            TribeAssigner ta = new TribeAssigner();
            ta.clearAssignments(campSessionId);
            loadTable(campSessionId);
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void loadCampSession() {
        List<CampSession> sessionList = registrar.getSessions();
        List<String> list = new ArrayList<String>();

        for (CampSession session : sessionList){
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

        int firstSessionId = SqliteCamperRegistrationDAO.NO_SESSIONS;

        if (registrar.getSessions() != null) {
            firstSessionId = registrar.getSessions().get(0).getCampSessioId();
        }

        loadTable(firstSessionId);

        sessions.setOnAction((event -> {
            CampSession session = getCampSessionFromUI();
            log.debug("User selected session {}", session.getShortDateString());
            loadTable(session.getCampSessioId());
        }));
    }

    private void loadTable(int campSessioId) {
        TribeAssignmentDAO tribeAssignmentDao = DAOFactory.createTribeAssignmentDAO();
        ObservableList<TribeAssignment> obList = null;

        try {
            list = tribeAssignmentDao.query(campSessioId);

            obList = FXCollections.observableList(list);
            tribeRostertable.getItems().clear();
            tribeRostertable.setItems(obList);
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

    public void backClicked() {
        log.debug("Back clicked");

        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }

    public void swapClicked() {
        TribeAssignment ta = tribeRostertable.getSelectionModel().getSelectedItem();

        if (ta == null) {
            displayNotice("Please select a tribe assignment.");
        } else {
            try {
                UIManager.getInstance().showTribeChangeAssignmentScreen(ta, tribeRostertable.getItems(), tribeRostertable);
            } catch (Exception e) {
                displayError(e);
            }
        }
    }

    public void assignClicked() {
        log.debug("Assign clicked");

        try {
            int campSessionId = getCampSessionFromUI().getCampSessioId();

            TribeAssigner ta = new TribeAssigner();
            ta.assign(2017, campSessionId);

            loadTable(campSessionId);
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void printClicked() {
        log.debug("Print clicked");
        TribeRosterGenerator trg = new TribeRosterGenerator();

        try {
            if (list != null) {
                trg.createTribeRosterPdf(list, "tribeRoster.Pdf");
            } else {
                displayError("No data to print");
            }

            String os = System.getProperty("os.name").toLowerCase();

            if (os.indexOf("mac") > 0) {
                Runtime.getRuntime().exec(new String[]{"open", "-a", "Preview", "tribeRoster.pdf"});
            } else {
                File myFile = new File("tribeRoster.pdf");
                Desktop.getDesktop().open(myFile);
            }
        } catch (Exception e) {
            displayError(e);
        }
    }
}
