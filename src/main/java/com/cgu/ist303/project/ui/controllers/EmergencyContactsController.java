package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.model.EmergencyContact;
import com.cgu.ist303.project.dao.EmergencyContactDAO;
import com.cgu.ist303.project.ui.UIManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class EmergencyContactsController extends BaseController implements Initializable {
    private static final Logger log = LogManager.getLogger(EmergencyContactsController.class);

    @FXML
    public TextField first1;
    public TextField middle1;
    public TextField last1;
    public TextField phone1a;
    public TextField phone1b;
    public TextField phone1c;
    public TextField first2;
    public TextField middle2;
    public TextField last2;
    public TextField phone2a;
    public TextField phone2b;
    public TextField phone2c;
    public TextField first3;
    public TextField middle3;
    public TextField last3;
    public TextField phone3a;
    public TextField phone3b;
    public TextField phone3c;

    public void initialize(URL url, ResourceBundle rb) {
    }

    private boolean isFirstContactComplete() {
        boolean isComplete = true;

        if (first1.getText() == null) {
            isComplete = false;
        } else if (first1.getText().equalsIgnoreCase("")) {
            isComplete = false;
        } else if (last1.getText() == null) {
            isComplete = false;
        } else if (last1.getText().equalsIgnoreCase("")) {
            isComplete = false;
        } else if ((phone1a.getText() == null) || (phone1b.getText() == null) || (phone1c.getText() == null)) {
            isComplete = false;
        } else if ((phone1a.getLength() != 3) || (phone1b.getLength() != 3) || (phone1c.getLength() != 4)) {
            isComplete = false;
        } else {
            isComplete = true;
        }

        return isComplete;
    }

    public void saveClicked() {
        UIManager.getInstance().closeCurrentScreenShowPrevious();

        if (isFirstContactComplete()) {
            EmergencyContactDAO dao = DAOFactory.createEmergencyContactDAO();
            EmergencyContact ec = new EmergencyContact();
            ec.setFirstName(first1.getText());

            if (middle1.getText() != null) {
                ec.setMiddleName(middle1.getText());
            }

            ec.setLastName(last1.getText());
            ec.setPhone1(phone1a.getText() + phone1b.getText() + phone1c.getText());

            try {
                dao.insert(ec);
                displayAlertMessage("Emergency contacts saved.");
            } catch (Exception e) {
                displayErrorMessage("Could not save emergency contacts", e);
            }

            cancelClicked();

            //TODO: Insert rest of emergency contacts
            //TODO: add support for alternate numbers
        } else {
            displayAlertMessage("First contact's information is not complete. Can not save contact information. Please complete the form for the first contact.");
        }
    }

    public void cancelClicked() {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
