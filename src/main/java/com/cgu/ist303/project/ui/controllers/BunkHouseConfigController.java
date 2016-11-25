package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.BunkHouseDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.model.BunkHouse;
import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.sqlite.SqliteCamperRegistrationDAO;
import com.cgu.ist303.project.registrar.Registrar;
import com.cgu.ist303.project.ui.UIManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class BunkHouseConfigController extends BaseController implements Initializable {
    private static final Logger log = LogManager.getLogger(BunkHouseConfigController.class);

    @FXML
    public TableView<BunkHouse> bunksTableView;
    @FXML
    public ComboBox<CampSession> sessions;

    private Registrar registrar = new Registrar();

    public void initialize(URL url, ResourceBundle rb) {
        bunksTableView.getColumns().clear();
        bunksTableView.setEditable(true);

        TableColumn idCol = new TableColumn("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<BunkHouse, String>("bunkHouseId"));

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<BunkHouse, String>("bunkHouseName"));

        TableColumn genderCol = new TableColumn("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<BunkHouse, String>("gender"));

        TableColumn capacityCol = new TableColumn("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<BunkHouse, String>("maxOccupants"));
        capacityCol.setStyle("-fx-alignment: CENTER_RIGHT;");

        bunksTableView.getColumns().addAll(idCol, nameCol, genderCol, capacityCol);

        try {
            registrar.load(2017);
            loadCampSession();
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void loadTable(int sessionId) throws Exception {
        BunkHouseDAO dao = DAOFactory.createBunkHouseDAO();
        ObservableList<BunkHouse> bunks = dao.query(sessionId);
        bunksTableView.setItems(bunks);
        bunksTableView.refresh();
    }

    private void loadCampSession() throws Exception {
        List<CampSession> sessionList = registrar.getSessions();
        List<String> list = new ArrayList<String>();

        for (CampSession session : sessionList){
            list.add(session.getShortDateString());
        }

        ObservableList obList = FXCollections.observableList(list);
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
            try {
                loadTable(session.getCampSessioId());
            } catch (Exception e) {
                displayError(e);
            }
        }));
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
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }

    public void addClicked() {
        try {
            CampSession session = getCampSessionFromUI();
            UIManager.getInstance().showBunkHouseScreen(null, session.getCampSessioId(), this.bunksTableView);
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void editClicked() {
        try {
            BunkHouse bh = bunksTableView.getSelectionModel().getSelectedItem();

            if (bh != null) {
                CampSession session = getCampSessionFromUI();
                UIManager.getInstance().showBunkHouseScreen(bh, session.getCampSessioId(), this.bunksTableView);
            } else {
                displayAlertMessage("Please select a bunk house.");
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void deleteClicked() {
        try {
            BunkHouse bh = bunksTableView.getSelectionModel().getSelectedItem();

            if (bh != null) {
                BunkHouseDAO dao = DAOFactory.createBunkHouseDAO();
                dao.delete(bh);

                bunksTableView.getItems().remove(bh);
                bunksTableView.refresh();
            } else {
                displayAlertMessage("Please select a bunk house.");
            }
        } catch (Exception e) {
            displayError(e);
        }
    }
}
