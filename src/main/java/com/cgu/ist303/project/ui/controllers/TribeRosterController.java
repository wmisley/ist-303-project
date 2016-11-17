package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.CamperRegistrationDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.TribeAssignmentDAO;
import com.cgu.ist303.project.dao.model.*;
import com.cgu.ist303.project.registrar.LetterGenerator;
import com.cgu.ist303.project.registrar.Registrar;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * Created by a on 10-11-2016.
 */
public class TribeRosterController implements Initializable {

    private static final Logger log = LogManager.getLogger(TribeRosterController.class);

    @FXML
    private TableView<TribeAssignment> tribeRostertable;
    @FXML
    private ComboBox<CampSession> sessions;
    @FXML
    private Button print;
    @FXML
    private Button back;
    @FXML
    private Button assign;
    List<TribeAssignment> list;

    private Registrar registrar = new Registrar();

    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<TribeAssignment, String> campersNameCol = new TableColumn<TribeAssignment, String>("Camper's Name");
        campersNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TribeAssignment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TribeAssignment, String> param) {
                return new SimpleStringProperty(param.getValue().getCamper().getFirstName());
            }
        });
        campersNameCol.setPrefWidth(246);
        TableColumn<TribeAssignment, Number> age = new TableColumn<TribeAssignment, Number>("Age");
        age.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TribeAssignment, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<TribeAssignment, Number> param) {
                return new SimpleIntegerProperty(param.getValue().getCamper().getAge());
            }
        });
        age.setPrefWidth(101);
        TableColumn<TribeAssignment, String> assignedTribe = new TableColumn<TribeAssignment, String>("Assigned Tribe");
        assignedTribe.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TribeAssignment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TribeAssignment, String> param) {
                return new SimpleStringProperty(param.getValue().getTribe().getTribeName());
            }
        });
        assignedTribe.setPrefWidth(238);

        tribeRostertable.getColumns().addAll(campersNameCol,age,assignedTribe);

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
//                loadTable(1);
            }
        }

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
            for (TribeAssignment assign : list) {
                log.debug(assign.getTribe().getTribeName());
            }
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

    public void assignClicked() {
        log.debug("Assign clicked");

        try {
            CamperRegistrationDAO dao = DAOFactory.createCamperRegistrationDAO();
            ObservableList<CamperRegistration> list = dao.queryRegisteredCampers(2017,
                    getCampSessionFromUI().getCampSessioId(), true);
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void displayError(Exception e) {
        e.printStackTrace();
        log.error(e);
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setContentText(e.getMessage());
        errorAlert.showAndWait();
    }

    public void printClicked() {
        log.debug("Print clicked");
        LetterGenerator lg = new LetterGenerator();
        try{
        lg.createTribeRosterPdf(list,"tribeRoster.Pdf");
            String os = System.getProperty("os.name").toLowerCase();

            if (os.indexOf("mac") > 0) {
                Runtime.getRuntime().exec(new String[]{"open", "-a", "Preview", "tribeRoster.pdf"});
            } else {
                File myFile = new File("tribeRoster.pdf");
                Desktop.getDesktop().open(myFile);
            }
        }catch (Exception e){
            displayError(e);
        }

    }
}
