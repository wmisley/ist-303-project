package com.cgu.ist303.project.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Stack;


public class UIManager {
    private static final Logger log = LogManager.getLogger(UIManager.class);
    static private UIManager uiManager = null;
    static private Stack<Stage> stageStack = new Stack<>();

    private UIManager() {
    }

    static public UIManager getInstance() {
        if (uiManager == null) {
            uiManager = new UIManager();
        }

        return uiManager;
    }

    private void showNewScreenHidePrevious(Stage current) {
        if (!stageStack.isEmpty()) {
            Stage prev = prev = stageStack.peek();
            prev.hide();
        }

        stageStack.push(current);
        current.show();
    }

    public void closeCurrentScreenShowPrevious() {
        if (!stageStack.isEmpty()) {
            Stage current = stageStack.pop();
            current.close();
        } else {
            log.warn("No current stage on stack");
        }

        if (!stageStack.isEmpty()) {
            Stage previous = stageStack.peek();
            previous.show();
        } else {
            log.warn("No current stage on stack");
        }
    }

    public void showApplicationScreen() throws Exception {
        log.debug("Showing application form");
        Stage stage = setStage("Camper Application Form", "/application.fxml", 600, 550);

        showNewScreenHidePrevious(stage);

        stage.show();
    }

    public void showPaymentScreen() throws Exception {
        log.debug("Showing payment form");
        Stage stage = setStage("Payment Form", "/payments.fxml", 600, 550);

        showNewScreenHidePrevious(stage);

        stage.show();
    }

    private Stage setStage(String title, String fxmlFile, int width, int height) throws Exception {
        Stage stage = new Stage();
        stage.setTitle(title);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

        Pane pane = loader.load();
        Scene scene = new Scene(pane, width, height);
        stage.setScene(scene);

        return stage;
    }

    public void showCheckinScreen() throws Exception {
        log.debug("Showing check-in form");
        Stage stage = setStage("Register Campers", "/checkin.fxml", 582, 515);

        showNewScreenHidePrevious(stage);

        stage.show();
    }

    public void showMainMenu(Stage stage) throws Exception {
        log.debug("Showing main menu form");


        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/menu.fxml"));
        Pane myPane = myLoader.load();
        Scene myScene = new Scene(myPane, 510, 370);

        stage.setTitle("Main Menu");
        stage.setScene(myScene);

        showNewScreenHidePrevious(stage);
    }

    public void showVerifyCheckinItems() throws Exception {
        log.debug("Showing verify check-in items form");
        Stage stage = setStage("Check List", "/verifycheckinitems.fxml", 376, 456);

        showNewScreenHidePrevious(stage);

        stage.show();
    }
}
