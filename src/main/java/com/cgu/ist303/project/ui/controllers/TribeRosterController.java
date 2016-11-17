package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.TribeAssignmentDAO;
import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.Tribe;
import com.cgu.ist303.project.dao.model.TribeAssignment;
import com.cgu.ist303.project.registrar.Registrar;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private Button printRoster;

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
            e.printStackTrace();
            log.error(e);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
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
            List<TribeAssignment> list = tribeAssignmentDao.query(campSessioId);
            for(TribeAssignment assign : list){
                log.debug(assign.getTribe().getTribeName());
            }
            obList = FXCollections.observableList(list);
            tribeRostertable.getItems().clear();
            tribeRostertable.setItems(obList);
        } catch (Exception e) {
            e.printStackTrace();
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
}

