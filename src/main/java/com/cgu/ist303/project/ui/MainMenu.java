package com.cgu.ist303.project.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/mainmenu.fxml"));
        primaryStage.setTitle("Camper Application Form");
        primaryStage.setScene(new Scene(root, 600, 550));
        primaryStage.show();
    }
}
