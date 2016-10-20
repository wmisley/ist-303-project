package com.cgu.ist303.project.ui.controllers;


import com.cgu.ist303.project.ui.UIManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {
    private static final Logger log = LogManager.getLogger(PaymentController.class);

    @FXML
    private Button ok;
    @FXML
    private Button cancel;
    @FXML
    private TextField payment;

    public void initialize(URL url, ResourceBundle rb) {
    }

    public void saveClicked() {
    }

    public void cancelClicked() {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
