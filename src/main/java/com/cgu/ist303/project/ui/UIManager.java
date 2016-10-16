package com.cgu.ist303.project.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class UIManager {
    private static final Logger log = LogManager.getLogger(UIManager.class);
    static private UIManager uiManager = null;
    static private Stage currentStage = null;
    static private Stage mainMenuStage = null;

    private UIManager() {
    }

    static public UIManager getInstance() {
        if (uiManager == null) {
            uiManager = new UIManager();
        }

        return uiManager;
    }

    public void showApplicationScreen() throws Exception {
        log.debug("Showing application form");

        Stage stage = new Stage();
        stage.setTitle("Camper Application Form");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application.fxml"));

        Pane pane = loader.load();
        Scene scene = new Scene(pane, 600, 550);
        stage.setScene(scene);

        mainMenuStage.hide();
        currentStage = stage;

        stage.show();
    }

    public void showPaymentScreen() throws Exception {
        log.debug("Showing payment form");

        Stage stage = new Stage();
        stage.setTitle("Payment Form");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/payments.fxml"));

        Pane pane = loader.load();
        Scene scene = new Scene(pane, 600, 550);
        stage.setScene(scene);

        mainMenuStage.hide();
        currentStage = stage;

        stage.show();
    }

    public void showCheckinScreen() throws Exception {
        log.debug("Showing check-in form");

        Stage stage = new Stage();
        stage.setTitle("Register Campers");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/checkin.fxml"));

        Pane pane = loader.load();
        Scene scene = new Scene(pane, 521, 515);
        stage.setScene(scene);

        mainMenuStage.hide();
        currentStage = stage;

        stage.show();
    }

    public void showMainMenu() throws Exception {
        log.debug("Showing main menu form");
        currentStage.close();
        mainMenuStage.show();
        currentStage = mainMenuStage;
    }

    public void showMainMenu(Stage stage) throws Exception {
        log.debug("Showing main menu form");
        currentStage = stage;
        mainMenuStage = stage;

        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/menu.fxml"));
        Pane myPane = myLoader.load();
        Scene myScene = new Scene(myPane, 510, 370);

        stage.setTitle("Main Menu");
        stage.setScene(myScene);
        stage.show();
    }
}
