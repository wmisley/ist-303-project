package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.TribeDAO;
import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.model.Tribe;
import com.cgu.ist303.project.dao.sqlite.SqliteCamperRegistrationDAO;
import com.cgu.ist303.project.registrar.Registrar;
import com.cgu.ist303.project.ui.UIManager;
import com.sun.javafx.collections.FloatArraySyncer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * Created by a on 25-11-2016.
 */
public class TribeConfigController extends BaseController implements Initializable {

    private static final Logger log = LogManager.getLogger(BunkHouseConfigController.class);

    @FXML
    public TableView<Tribe> tribeTableView;
    @FXML
    public ComboBox<CampSession> sessions;

    Registrar registrar = new Registrar();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tribeTableView.setEditable(true);

        TableColumn idCol = new TableColumn("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<Tribe, String>("tribeId"));

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<Tribe, String>("tribeName"));

        tribeTableView.getColumns().addAll(idCol, nameCol);

        try{
            registrar.load(2017);
            loadCampSessions();
        }catch (Exception e){
            displayError(e);
        }

    }

    private void loadCampSessions() throws Exception {
        List<CampSession> sessionList = registrar.getSessions();
        List<String> list = new ArrayList<String>();

        for (CampSession session : sessionList){
            list.add(session.getShortDateString());
        }

        ObservableList obList = FXCollections.observableList(list);
        sessions.setItems(obList);

        if (sessions.getItems() != null){
            if (sessions.getItems().size() > 0){
                sessions.getSelectionModel().select(0);
            }
        }

        int firstSessionId = SqliteCamperRegistrationDAO.NO_SESSIONS;

        if( registrar.getSessions() != null){
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

    private CampSession getCampSessionFromUI(){
        int index = sessions.getSelectionModel().getSelectedIndex();
        CampSession campSession = null;

        if (index >= 0) {
            campSession = registrar.getSessions().get(index);
        }

        return campSession;
    }

    private void loadTable(int sessionId) throws Exception {
        TribeDAO tribeDao = DAOFactory.createTribeDAO();
        ObservableList<Tribe> tribes = tribeDao.query(sessionId);
        tribeTableView.setItems(tribes);
        tribeTableView.refresh();
    }

    public void addClicked(ActionEvent actionEvent) {
        try{
            CampSession session = getCampSessionFromUI();
            UIManager.getInstance().showTribeScreen(null, session.getCampSessioId(), tribeTableView);
        }catch (Exception e){
            displayError(e);
        }
    }

    public void editClicked(ActionEvent actionEvent) {
        try{
            Tribe tribe = tribeTableView.getSelectionModel().getSelectedItem();
            if (tribe != null) {
                CampSession session = getCampSessionFromUI();
                UIManager.getInstance().showTribeScreen(tribe, session.getCampSessioId(), this.tribeTableView);
            } else {
                displayAlertMessage("Please select a bunk house.");
            }
        }catch (Exception e){
            displayError(e);
        }
    }

    public void deleteClicked(ActionEvent actionEvent) {
        try {
            Tribe tribe = tribeTableView.getSelectionModel().getSelectedItem();

            if (tribe != null) {
                TribeDAO dao = DAOFactory.createTribeDAO();
                dao.delete(tribe);

                tribeTableView.getItems().remove(tribe);
                tribeTableView.refresh();
            } else {
                displayAlertMessage("Please select a bunk house.");
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void backClicked(ActionEvent actionEvent) {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
