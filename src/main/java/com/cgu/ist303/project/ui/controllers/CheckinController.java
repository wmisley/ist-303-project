package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.CamperRegistrationDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import com.cgu.ist303.project.dao.sqlite.SqliteCamperRegistrationDAO;
import com.cgu.ist303.project.registrar.RefundCalculator;
import com.cgu.ist303.project.registrar.Registrar;
import com.cgu.ist303.project.ui.UIManager;
import com.cgu.ist303.project.ui.model.EquipmentItem;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.PropertySheet;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;


public class CheckinController extends BaseController implements Initializable {
    private static final Logger log = LogManager.getLogger(CheckinController.class);

    @FXML
    private TableView<CamperRegistration> campersTable;
    @FXML
    private ComboBox<CampSession> sessions;
    @FXML
    private Button makePayment;
    @FXML
    private Button emergencyContacts;
    @FXML
    public Button cancellation;

    private Registrar registrar = new Registrar();

    public void initialize(URL url, ResourceBundle rb) {
        campersTable.getColumns().clear();
        campersTable.setEditable(true);

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Camper, String>("firstName"));
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Camper, String>("lastName"));
        TableColumn phoneNumber = new TableColumn("Phone Number");
        phoneNumber.setCellValueFactory(new PropertyValueFactory<Camper, String>("phoneNumberString"));
        TableColumn age = new TableColumn("Age");
        age.setCellValueFactory(new PropertyValueFactory<Camper, String>("age"));
        age.setStyle("-fx-alignment: CENTER_RIGHT;");
        TableColumn gender = new TableColumn("Gender");
        gender.setCellValueFactory(new PropertyValueFactory<Camper, String>("gender"));
        TableColumn payment = new TableColumn("Amount Due");
        payment.setCellValueFactory(new PropertyValueFactory<Camper, String>("formattedAmountDue"));
        payment.setStyle("-fx-alignment: CENTER_RIGHT;");
        TableColumn regDateCol = new TableColumn("Registered");
        regDateCol.setCellValueFactory(new PropertyValueFactory<Camper, String>("registrationDateString"));
        TableColumn isCheckedIn = new TableColumn("Checked-in");
        isCheckedIn.setCellValueFactory(new PropertyValueFactory<Camper, String>("checkedInStatus"));

        campersTable.getColumns().addAll(firstNameCol, lastNameCol, phoneNumber, age, gender, payment, regDateCol, isCheckedIn);

        try {
            registrar.load(2017);
            loadCampSessions();

            int firstSessionId = SqliteCamperRegistrationDAO.NO_SESSIONS;

            if (registrar.getSessions() != null) {
                firstSessionId = registrar.getSessions().get(0).getCampSessioId();
            }

            loadTable(firstSessionId);
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void emergencyContactsClicked() throws Exception {
        UIManager.getInstance().showEmergencyContactsScreen();
    }

    private void loadTable(int sessionId) {
        CamperRegistrationDAO regDAO = DAOFactory.createCamperRegistrationDAO();
        ObservableList<CamperRegistration> obsList  = null;

        try {

            obsList = regDAO.queryRegisteredCampers(2017, sessionId, false);
            campersTable.getItems().clear();
            campersTable.setItems(obsList);
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

    private void loadCampSessions() {
        List<CampSession> sessionList = registrar.getSessions();
        List<String> list = new ArrayList<String>();

        for (CampSession session : sessionList) {
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

        sessions.setOnAction((event) -> {
            CampSession session = getCampSessionFromUI();
            log.debug("User selected session {}", session.getShortDateString());

            loadTable(session.getCampSessioId());
        });
    }

    public void checkinClicked() throws Exception {
        CamperRegistration cr = campersTable.getSelectionModel().getSelectedItem();

        if (cr != null) {
            log.debug("Camper {} {} was selected for check-in by the user", cr.getFirstName(),
                    cr.getLastName());

            if (cr.getAmountDue() > 0.0) {
                displayNotice(String.format("The camper can not check-in until they pay $%s",
                        String.format("%,.2f", cr.getAmountDue())));
            } else {
                UIManager.getInstance().showVerifyCheckinItems(cr);
                campersTable.refresh();
            }
        } else {
            displayNotice("Please select a camper.");
        }
    }

    public void makePaymentClicked() throws Exception {
        CamperRegistration cr = campersTable.getSelectionModel().getSelectedItem();

        if (cr != null) {
            log.debug("Camper {} {} was selected for making a payment by the user", cr.getFirstName(),
                    cr.getLastName());

            if (cr.getAmountDue() > 0.0) {
                UIManager.getInstance().showPayScreen(cr, campersTable);
            } else {
                displayNotice("This camper has paid the full amount. No need to make a payment.");
            }
        } else {
            displayNotice("Please select a camper.");
        }
    }

    public void cancelClicked() throws Exception {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }

    public void refundClicked() {
        CamperRegistration cr = campersTable.getSelectionModel().getSelectedItem();
        CampSession session = getCampSessionFromUI();
        if(cr != null && session != null){
            try{
                if(cr.getAmountDue() < 0){
                    CamperRegistrationDAO dao = DAOFactory.createCamperRegistrationDAO();
                    dao.update(cr.getCamperId(), session.getCampSessioId(), cr.getPayment() + cr.getAmountDue());
                    displayNotice("Issue a refund of $" + Double.toString(-1 * cr.getAmountDue()));
                    campersTable.refresh();
                    loadTable(session.getCampSessioId());
                }else{
                    displayNotice("You do not have any refund.");
                }
            }catch(Exception e){
                displayError(e);
            }
        }else{
            displayNotice("Please select a camper");
        }
    }

    public void cancellationClicked() {
        log.info("User has selected to cancel a camper registration.");

        CamperRegistration cr = campersTable.getSelectionModel().getSelectedItem();
        CampSession session = getCampSessionFromUI();

        if ((cr != null) && (session != null)) {
            try {
                RefundCalculator rc = new RefundCalculator(cr.getRegistrationDate(), new Date());
                long days = rc.calculateNumberOfDays();
                double refund = rc.calculteRefund(cr.getPayment());
                double percentage = rc.calculatePercentageRefund() * 100;

                log.info("Paid={}, Days since acceptance={}, Calculated refund={}, Calculated percentage={}",
                        cr.getAmountDue(), days, refund, percentage);
                String message =String.format(
                        "The camper registration is cancelled. It has been %d days since the acceptance letter and " +
                        "arrival packet has been sent. Therefore the camper will only get a %.0f%% refund. Please " +
                        "refund the camper $%.2f.", days, percentage, refund);
                displayNotice(message);

                CamperRegistrationDAO dao = DAOFactory.createCamperRegistrationDAO();
                dao.delete(cr.getCamperId(), session.getCampSessioId());
                campersTable.getItems().remove(cr);
                campersTable.refresh();
            } catch (Exception e) {
                displayError(e);
            }
        } else {
            displayNotice("Please select a camper.");
        }
    }
}
