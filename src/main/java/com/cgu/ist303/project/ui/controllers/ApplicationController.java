package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.*;
import com.cgu.ist303.project.dao.model.*;
import com.cgu.ist303.project.registrar.Registrar;
import com.cgu.ist303.project.ui.UIManager;
import com.cgu.ist303.project.ui.controls.LimitedNumberTextField;
import com.cgu.ist303.project.ui.controls.LimitedTextField;
import com.cgu.ist303.project.ui.controls.NumberTextField;
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


public class ApplicationController implements Initializable {
    private static final Logger log = LogManager.getLogger(ApplicationController.class);

    @FXML
    private LimitedTextField camperFirstName;
    @FXML
    private LimitedTextField camperMiddleName;
    @FXML
    private LimitedTextField camperLastName;
    @FXML
    private LimitedTextField parentFirstName;
    @FXML
    private LimitedTextField parentMiddleName;
    @FXML
    private LimitedTextField parentLastName;
    @FXML
    private LimitedTextField streetLine1;
    @FXML
    private LimitedTextField streetLine2;
    @FXML
    private LimitedTextField city;
    @FXML
    private ComboBox<String> state;
    @FXML
    private LimitedNumberTextField zip;
    @FXML
    private ComboBox<String> gender;
    @FXML
    private ComboBox<String> age;
    @FXML
    private LimitedNumberTextField phone1;
    @FXML
    private LimitedNumberTextField phone2;
    @FXML
    private LimitedNumberTextField phone3;
    @FXML
    private ComboBox<String> session;
    @FXML
    private LimitedNumberTextField dollars;
    
    private Registrar registrar = new Registrar();

    public void initialize(URL url, ResourceBundle rb) {
        try {
            registrar.load(2016);
        } catch (Exception e) {
            log.error(e);
        }

        camperFirstName.setMaxlength(25);
        camperMiddleName.setMaxlength(25);
        camperLastName.setMaxlength(25);
        parentFirstName.setMaxlength(25);
        parentMiddleName.setMaxlength(25);
        parentLastName.setMaxlength(25);
        streetLine1.setMaxlength(75);
        streetLine2.setMaxlength(75);
        city.setMaxlength(50);
        zip.setMaxlength(5);
        phone1.setMaxlength(3);
        phone2.setMaxlength(3);
        phone3.setMaxlength(4);
        dollars.setMaxlength(5);

        loadCampSessions();
    }

    private boolean isFormComplete() {
        String message = "The form is not complete. \n\n";

        if (camperFirstName.getLength() <= 0) {
            message += "    - Camper First Name is blank";
        }
        if (camperLastName.getLength() <= 0) {
            message += "    - Camper Last Name is blank";
        }
        if (parentFirstName.getLength() <= 0) {
            message += "    - Responsible Party First Name is blank";
        }
        if (parentLastName.getLength() <= 0) {
            message += "    - Responsible Party Last Name is blank";
        }
        if (age.getSelectionModel().getSelectedIndex() < 0) {
            message += "    - Age is not specified";
        }
        if (age.getSelectionModel().getSelectedIndex() < 0) {
            message += "    - Age is not specified";
        }

        return true;
    }

    public void cancelClicked() throws Exception {
        UIManager.getInstance().showMainMenu();
    }

    private void loadCampSessions() {
        List<CampSession> sessionList = registrar.getSessions();
        List<String> list = new ArrayList<String>();

        for (CampSession session : sessionList) {
            String sessionValue = String.format("%d/%d to %d/%d",
                    session.getStartMonth(), session.getStartDay(),
                    session.getEndMonth(), session.getEndDay());
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

        //10=other, 11=not specified
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
