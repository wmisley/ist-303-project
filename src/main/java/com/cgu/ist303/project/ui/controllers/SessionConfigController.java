package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import com.cgu.ist303.project.ui.UIManager;
import com.cgu.ist303.project.ui.controls.LimitedNumberTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class SessionConfigController extends BaseController implements Initializable {
    private static final Logger log = LogManager.getLogger(SessionConfigController.class);

    @FXML
    public TableView<CampSession> csTable = null;
    @FXML
    public Button add;
    @FXML
    public Button edit;
    @FXML
    public Button delete;
    @FXML
    public Button back;
    @FXML
    public ComboBox<String> year;

    public void initialize(URL url, ResourceBundle rb)  {
    }

    public void addClicked() {
    }

    public void deleteClicked() {
    }

    public void editClicked() {
    }

    public void backClicked() {
        log.info("Back clicked");
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
