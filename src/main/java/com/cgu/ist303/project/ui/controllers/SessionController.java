package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.CampSessionDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.ui.UIManager;
import com.cgu.ist303.project.ui.controls.LimitedNumberTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SessionController extends BaseController implements Initializable {
    private static final Logger log = LogManager.getLogger(SessionController.class);
    private CampSession cs = null;
    private int campYear = 0;
    private TableView<CampSession> tableView = null;

    @FXML
    public Button save;
    @FXML
    public Button cancel;
    @FXML
    public DatePicker start;
    @FXML
    public DatePicker end;
    @FXML
    public LimitedNumberTextField capacity;

    public void setSession(CampSession session, int y, TableView<CampSession> tbl) {
        cs = session;
        campYear = y;
        tableView = tbl;

        if (cs != null) {
            start.setValue(create(cs.getStartDay(), cs.getEndMonth(), cs.getCampYear()));
            end.setValue(create(cs.getEndDay(), cs.getEndMonth(), cs.getCampYear()));
            capacity.setText(Integer.toString(cs.getGenderLimit()));
        }
    }

    public LocalDate create(int day, int month, int year) {
        String dayString = day >= 10 ? Integer.toString(day) : "0" + Integer.toString(day);
        String monthString = month >= 10 ? Integer.toString(month): "0" + Integer.toString(month);

        String dateString = dayString + "-" + monthString + "-" + Integer.toString(year);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }

    public void initialize(URL url, ResourceBundle rb)  {
    }

    private CampSession getSessionFromUI() {
        CampSession session = new CampSession();

        session.setGenderLimit(Integer.parseInt(capacity.getText()));
        session.setCampYear(campYear);
        session.setStartDay(start.getValue().getDayOfMonth());
        session.setStartMonth(start.getValue().getMonthValue());
        session.setEndDay(end.getValue().getDayOfMonth());
        session.setEndMonth(end.getValue().getMonthValue());

        return session;
    }

    private boolean isInputValid() {
        if (start.getValue() == null ) {
            displayNotice("Please select a start date.");
        } else if (end.getValue() == null) {
            displayNotice("Please select an end date.");
        } else if (!end.getValue().isAfter(start.getValue())) {
            displayNotice("Start date can not be after the end date.");
        } else if (capacity.getText() == null) {
            displayNotice("Capacity should be a positive number.");
        } else if (capacity.getText().equalsIgnoreCase("")) {
            displayNotice("Capacity should be a positive number.");
        } else if (Integer.parseInt(capacity.getText()) <= 0) {
            displayNotice("Capacity should be a positive number.");
        } else if (end.getValue().getYear() != campYear) {
            displayNotice("The end date must be in the year " + Integer.toString(campYear) + ".");
        } else if (start.getValue().getYear() != campYear) {
            displayNotice("The start date must be in the year " + Integer.toString(campYear) + ".");
        } else {
            return true;
        }

        return false;
    }

    public void saveClicked() {
        if (isInputValid()) {
            try {
                CampSessionDAO dao = DAOFactory.createCampSessionDAO();

                CampSession campSession = getSessionFromUI();
                int sessionId = dao.insert(campSession);
                campSession.setCampSessioId(sessionId);

                if (cs != null) {
                    cs.setGenderLimit(Integer.parseInt(capacity.getText()));
                    cs.setCampYear(campYear);
                    cs.setStartDay(start.getValue().getDayOfMonth());
                    cs.setStartMonth(start.getValue().getMonthValue());
                    cs.setEndDay(end.getValue().getDayOfMonth());
                    cs.setEndMonth(end.getValue().getMonthValue());
                } else {
                    tableView.getItems().add(campSession);
                    tableView.refresh();
                }

                UIManager.getInstance().closeCurrentScreenShowPrevious();
            } catch (Exception e) {
                displayError(e);
            }
        }
    }

    public void cancelClicked() {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }

}
