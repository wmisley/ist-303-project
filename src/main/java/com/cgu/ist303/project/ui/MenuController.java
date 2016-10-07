package com.cgu.ist303.project.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MenuController implements Initializable {
    private static final Logger log = LogManager.getLogger(MenuController.class);

    private Stage prevStage;

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    public void initialize(URL url, ResourceBundle rb) {
    }

    public void showApplication(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Camper Application Form");
        Pane myPane = FXMLLoader.load(getClass().getResource("/application.fxml"));
        Scene scene = new Scene(myPane);
        stage.setScene(scene);

        prevStage.close();

        stage.show();
    }
}
