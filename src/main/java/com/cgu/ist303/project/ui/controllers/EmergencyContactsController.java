package com.cgu.ist303.project.ui.controllers;

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
    private TextField first1;
    private TextField middle1;
    private TextField last1;
    private TextField phone1a;
    private TextField phone1b;
    private TextField phone1c;
    private TextField first2;
    private TextField middle2;
    private TextField last2;
    private TextField phone2a;
    private TextField phone2b;
    private TextField phone2c;
    private TextField first3;
    private TextField middle3;
    private TextField last3;
    private TextField phone3a;
    private TextField phone3b;
    private TextField phone3c;

    public void initialize(URL url, ResourceBundle rb) {
    }

    public void saveClicked() {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }

    public void cancelClicked() {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
