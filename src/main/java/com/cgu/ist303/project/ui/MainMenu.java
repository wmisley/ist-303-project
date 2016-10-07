package com.cgu.ist303.project.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainMenu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //@Override
    public void start2(Stage primaryStage) throws Exception{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/application.fxml"));

        Pane myPane = myLoader.load();
        Scene myScene = new Scene(myPane, 600, 550);


        primaryStage.setTitle("Camper Application Form");

        ApplicationController controller = myLoader.getController();
        controller.setPrevStage(primaryStage);

        primaryStage.setScene(myScene);
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/menu.fxml"));

        Pane myPane = myLoader.load();
        Scene myScene = new Scene(myPane, 510, 370);


        primaryStage.setTitle("Main Menu");

        MenuController controller = myLoader.getController();
        controller.setPrevStage(primaryStage);

        primaryStage.setScene(myScene);
        primaryStage.show();
    }
}
