package com.cgu.ist303.project.ui;

import com.cgu.ist303.project.dao.model.Camper;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Created by will4769 on 10/3/16.
 */
public class MainMenuController {
    @FXML
    private TextField camperFirstName;
    @FXML
    private TextField camperMiddleName;
    @FXML
    private TextField camperLastName;
    @FXML
    private TextField parentFirstName;
    @FXML
    private TextField parentMiddleName;
    @FXML
    private TextField parentLastName;

    public void saveClicked() {
        Camper camper = new Camper();
        camper.setFirstName(camperFirstName.getText());
        camper.setMiddleName(camperMiddleName.getText());
        camper.setLastName(camperLastName.getText());
        camper.setRpFirstName(parentFirstName.getText());
        camper.setRpMiddleName(parentMiddleName.getText());
        camper.setRpLastName(parentLastName.getText());
    }
}
