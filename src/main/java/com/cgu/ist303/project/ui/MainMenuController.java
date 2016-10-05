package com.cgu.ist303.project.ui;

import com.cgu.ist303.project.dao.CampSessionDAO;
import com.cgu.ist303.project.dao.CamperDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.model.Camper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by will4769 on 10/3/16.
 */
public class MainMenuController implements Initializable {
    private static final Logger log = LogManager.getLogger(MainMenuController.class);

    @FXML
    private TextField camperFirstName;
    @FXML
    private TextField camperMiddleName;
    @FXML
    private TextField camperLastName;
    @FXML
    private TextField parentFirstName;
    @FXML
    private TextField parentMiddleName;
    @FXML
    private TextField parentLastName;
    @FXML
    private TextField streetLine1;
    @FXML
    private TextField streetLine2;
    @FXML
    private TextField city;
    @FXML
    private ComboBox<String> state;
    @FXML
    private TextField zip;
    @FXML
    private ComboBox<String> gender;
    @FXML
    private ComboBox<String> age;
    @FXML
    private TextField phone1;
    @FXML
    private TextField phone2;
    @FXML
    private TextField phone3;
    @FXML
    private ComboBox<String> session;

    public void initialize(URL url, ResourceBundle rb) {
        DAOFactory.dbPath = "ist303.db";
        List<CampSession> sessionList = new ArrayList<CampSession>();

        try {
            log.debug("Querying camp sessions");

            CampSessionDAO sessionDAO = DAOFactory.createCampSessionDAO();
            sessionList = sessionDAO.query(2016);
        } catch (Exception e) {
            log.error(e);
        }

        List<String> list = new ArrayList<String>();

        for (CampSession session : sessionList) {
            String sessionValue = String.format("%d/%d to %d/%d",
                    session.getStartDay(), session.getStartMonth(),
                    session.getEndMonth(), session.getEndDay());
            list.add(sessionValue);
        }

        ObservableList obList = FXCollections.observableList(list);
        session.getItems().clear();
        session.setItems(obList);
    }

    public void saveClicked() throws Exception {
        Camper camper = new Camper();
        camper.setFirstName(camperFirstName.getText());
        camper.setMiddleName(camperMiddleName.getText());
        camper.setLastName(camperLastName.getText());
        camper.setRpFirstName(parentFirstName.getText());
        camper.setRpMiddleName(parentMiddleName.getText());
        camper.setRpLastName(parentLastName.getText());

        if (age.getSelectionModel().getSelectedIndex() >= 0) {
            camper.setAge(Integer.parseInt(age.getSelectionModel().getSelectedItem()));
        } else {
            throw new Exception("Age not specified");
        }

        if (gender.getSelectionModel().getSelectedIndex() == 0) {
            camper.setGender(Camper.Gender.Male);
        } else if (gender.getSelectionModel().getSelectedIndex() == 1) {
            camper.setGender(Camper.Gender.Female);
        } else {
            throw new Exception("Gender not specified");
        }

        if (state.getSelectionModel().getSelectedIndex() >= 0) {
            camper.setState(state.getSelectionModel().getSelectedItem());
        } else {
            throw new Exception("State not specified");
        }

        camper.setStreet(streetLine1.getText());
        camper.setCity(city.getText());
        camper.setZipCode(zip.getText());
        camper.setPhoneNumber(phone1.getText() + phone2.getText() + phone3.getText());

        CamperDAO camperDAO = DAOFactory.createCamperDAO();

        try {
            log.debug("Inserting new camper record");
            int camperId = camperDAO.insertCamper(camper);
            camper.setCamperId(camperId);
        } catch (Exception e) {
            log.error(e);
        }
    }
}