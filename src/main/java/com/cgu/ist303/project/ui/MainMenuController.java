package com.cgu.ist303.project.ui;

import com.cgu.ist303.project.dao.*;
import com.cgu.ist303.project.dao.model.*;
import com.cgu.ist303.project.registrar.Registrar;
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
    @FXML
    private TextField dollars;
    
    private Registrar registrar = new Registrar();

    public void initialize(URL url, ResourceBundle rb) {
        DAOFactory.dbPath = "ist303.db";

        try {
            registrar.load(2016);
        } catch (Exception e) {
            log.error(e);
        }

        loadCampSessions();
    }

    private void loadCampSessions() {
        List<CampSession> sessionList = registrar.getSessions();
        List<String> list = new ArrayList<String>();

        for (CampSession session : sessionList) {
            String sessionValue = String.format("%d/%d to %d/%d",
                    session.getStartDay(), session.getStartMonth(),
                    session.getEndDay(), session.getEndMonth());
            list.add(sessionValue);
        }

        ObservableList obList = FXCollections.observableList(list);
        session.getItems().clear();
        session.setItems(obList);
    }

    private Camper insertCamperRecord() throws Exception {
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

        return camper;
    }

    public int registerCamper(Camper camper) throws Exception {
        int index = session.getSelectionModel().getSelectedIndex();
        int campSessionId = -1;

        if (index >= 0) {
            CampSession campSession = registrar.getSessions().get(index);
            campSessionId = campSession.getCampSessioId();

            registrar.processApplication(camper, campSession);
        } else {
            throw new Exception("Need to handle no selected session");
        }

        return campSessionId;
    }

    public void insertPayment(int camperId, int sessionId) {
        double payValue = Double.parseDouble(dollars.getText());

        PaymentDAO dao = DAOFactory.createPaymentDAO();
        Payment payment = new Payment();
        payment.setCamperId(camperId);
        payment.setCampSessionId(sessionId);
        payment.setAmount(payValue);

        try {
            log.debug("Inserting payment record");
            dao.insert(payment);
        } catch (Exception e) {
            log.error(e);
        }
    }


    public void saveClicked() throws Exception {
        //TODO: validate form inputs

        //TODO: Check if camper exists
        Camper camper = insertCamperRecord();

        //TODO: Check if camper registered
        int sessionId = registerCamper(camper);

        insertPayment(camper.getCamperId(), sessionId);
        //TODO: If rejected, send rejection notice
    }
}
