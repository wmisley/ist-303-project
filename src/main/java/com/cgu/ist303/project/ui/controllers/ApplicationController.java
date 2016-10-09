package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.*;
import com.cgu.ist303.project.dao.model.*;
import com.cgu.ist303.project.registrar.Registrar;
import com.cgu.ist303.project.ui.UIManager;
import com.cgu.ist303.project.ui.controls.LimitedNumberTextField;
import com.cgu.ist303.project.ui.controls.LimitedTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    private String getValidationMessage() {
        String message = "";

        if (camperFirstName.getLength() <= 0) {
            message += "\n    - Camper First Name is blank";
        }
        if (camperLastName.getLength() <= 0) {
            message += "\n    - Camper Last Name is blank";
        }
        if (parentFirstName.getLength() <= 0) {
            message += "\n    - Responsible Party First Name is blank";
        }
        if (parentLastName.getLength() <= 0) {
            message += "\n    - Responsible Party Last Name is blank";
        }
        if (age.getSelectionModel().getSelectedIndex() < 0) {
            message += "\n    - Age is not specified";
        }
        if (gender.getSelectionModel().getSelectedIndex() < 0) {
            message += "\n    - Gender is not specified";
        }
        if (streetLine1.getLength() <= 0) {
            message += "\n    - Street Address Line 1 is blank";
        }
        if (city.getLength() <= 0) {
            message += "\n    - City is blank";
        }
        if (zip.getLength() != 5) {
            message += "\n    - Zipcode must be 5 numbers";
        }
        if (state.getSelectionModel().getSelectedIndex() < 0) {
            message += "\n    - State is not specified";
        }
        if ((phone1.getLength() != 3) || (phone2.getLength() != 3) || (phone3.getLength() != 4)) {
            message += "\n    - Phone must be 10 numbers";
        }
        if (session.getSelectionModel().getSelectedIndex() < 0) {
            message += "\n    - Camp Session is not specified";
        }

        return message;
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
        if (age.getSelectionModel().getSelectedIndex() == 10) {
            camper.setAge(Camper.NOT_IN_AGE_RANGE);
        } else if (age.getSelectionModel().getSelectedIndex() == 11) {
            camper.setAge(Camper.NOT_SPECIFIED);
        } else if (age.getSelectionModel().getSelectedIndex() >= 0) {
            camper.setAge(Integer.parseInt(age.getSelectionModel().getSelectedItem()));
        } else {
            camper.setAge(Camper.NOT_SPECIFIED);
        }

        if (gender.getSelectionModel().getSelectedIndex() == 0) {
            camper.setGender(Camper.Gender.Male);
        } else if (gender.getSelectionModel().getSelectedIndex() == 1) {
            camper.setGender(Camper.Gender.Female);
        } else {
            camper.setGender(Camper.Gender.Unspecified);
        }

        if (state.getSelectionModel().getSelectedIndex() == 50) {
            camper.setState("");
        } else if (state.getSelectionModel().getSelectedIndex() >= 0) {
            camper.setState(state.getSelectionModel().getSelectedItem());
        } else {
            camper.setState("");
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

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }

        return camper;
    }

    public int registerCamper(Camper camper) throws Exception {
        int index = session.getSelectionModel().getSelectedIndex();
        int campSessionId = -1;

        if (index >= 0) {
            CampSession campSession = registrar.getSessions().get(index);
            campSessionId = campSession.getCampSessioId();

            RejectedApplication.RejectionReason rejectReason =
                    registrar.processApplication(camper, campSession);

            if (rejectReason == RejectedApplication.RejectionReason.NotRejected) {
                insertPayment(camper.getCamperId(), campSessionId);
            }
        } else {
            throw new Exception("No session selected");
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

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }


    public void saveClicked() throws Exception {
        String message = getValidationMessage();

        if (!message.equalsIgnoreCase("")) {
            log.debug("Form not complete, prompting user to reject application or complete form");
            message = "Press OK to reject the application, press Cancel to go back.\n" + message;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Form Not Complete");
            alert.setHeaderText("If the form is not completed, the application will be rejected.");
            alert.setContentText(message);
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    try {
                        //TODO: Check if camper exists
                        Camper camper = insertCamperRecord();

                        //TODO: Check if camper registered
                        int sessionId = registerCamper(camper);
                    } catch (Exception e) {
                        log.error(e);

                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setContentText(e.getMessage());
                        errorAlert.showAndWait();
                    }
                }
            });
        }

        //TODO: Reject application if it isn't compete
    }
}
