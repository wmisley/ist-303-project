package com.cgu.ist303.project.ui;

import com.cgu.ist303.project.dao.DAOFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CampRegistrationApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        DAOFactory.dbPath = "ist303.db";

        UIManager.getInstance().showMainMenu(primaryStage);
    }

}
