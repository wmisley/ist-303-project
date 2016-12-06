package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import com.cgu.ist303.project.dao.model.Tribe;
import com.cgu.ist303.project.registrar.Registrar;
import com.cgu.ist303.project.ui.UIManager;
import com.cgu.ist303.project.ui.controls.LimitedNumberTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class SessionConfigController extends BaseController implements Initializable {
    private static final Logger log = LogManager.getLogger(SessionConfigController.class);

    @FXML
    public TableView<CampSession> csTableView;
    @FXML
    public Button add;
    @FXML
    public Button edit;
    @FXML
    public Button delete;
    @FXML
    public Button back;
    @FXML
    public ComboBox<String> year;

    public void initialize(URL url, ResourceBundle rb)  {
        loadYears();
        buildTableHeaders();

        int yy = Integer.parseInt(year.getSelectionModel().getSelectedItem());
        loadTable(yy);

        year.setOnAction((event -> {
            int y = Integer.parseInt(year.getSelectionModel().getSelectedItem());
            log.debug("User selected year {}", y);
            try {
                loadTable(y);
            } catch (Exception e) {
                displayError(e);
            }
        }));
    }

    private void buildTableHeaders() {
        TableColumn idCol = new TableColumn("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<CampSession, String>("campSessioId"));

        TableColumn dateCol = new TableColumn("Dates");
        dateCol.setCellValueFactory(new PropertyValueFactory<CampSession, String>("longDateString"));

        TableColumn glCol = new TableColumn("Gender Limit");
        glCol.setCellValueFactory(new PropertyValueFactory<CampSession, String>("genderLimit"));

        csTableView.getColumns().clear();
        csTableView.getColumns().addAll(idCol, dateCol, glCol);
    }

    private void loadYears() {
        List<String> years = new ArrayList<>();
        int y = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = 2017; i <= (y + 5); i++) {
            years.add(Integer.toString(i));
        }

        ObservableList<String> obsYears = FXCollections.observableArrayList(years);
        year.setItems(obsYears);
        year.getSelectionModel().select(0);
    }

    private void loadTable(int year) {
        Registrar registrar = new Registrar();

        try {
            registrar.load(year);
            List<CampSession> sessions = registrar.getSessions();
            ObservableList<CampSession> obsSessions = FXCollections.observableList(sessions);
            csTableView.setItems(obsSessions);
            csTableView.refresh();
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void addClicked() {
        try {
            UIManager.getInstance().showSessionScreen(null);
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void deleteClicked() {
    }

    public void editClicked() {
        try {
            CampSession session = csTableView.getSelectionModel().getSelectedItem();

            if (session != null) {
                UIManager.getInstance().showSessionScreen(session);
                csTableView.refresh();
            } else {
                displayNotice("You must first selected a camp session.");
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void backClicked() {
        log.info("Back clicked");
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
