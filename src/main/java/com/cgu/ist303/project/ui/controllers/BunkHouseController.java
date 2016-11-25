package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.model.BunkHouse;
import com.cgu.ist303.project.ui.UIManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class BunkHouseController extends BaseController implements Initializable {
    private static final Logger log = LogManager.getLogger(BunkHouseController.class);

    public void initialize(URL url, ResourceBundle rb) {
    }

    public void okClicked() {

    }

    public void cancelClicked() {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
