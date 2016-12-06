package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.ui.UIManager;
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

    @FXML
    public Button save;
    @FXML
    public Button cancel;
    @FXML
    public DatePicker start;
    @FXML
    public DatePicker end;
    @FXML
    public TextField capacity;

    public void setSession(CampSession session) {
        cs = session;

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

    public void saveClicked() {
    }

    public void cancelClicked() {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }

}
