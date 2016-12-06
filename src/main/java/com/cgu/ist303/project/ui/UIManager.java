package com.cgu.ist303.project.ui;

import com.cgu.ist303.project.dao.model.BunkHouse;
import com.cgu.ist303.project.dao.model.BunkHouseAssignment;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import com.cgu.ist303.project.dao.model.Tribe;
import com.cgu.ist303.project.ui.controllers.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
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
        showNewScreenHidePrevious(current, true, false);
    }

    private void showNewScreenHidePrevious(Stage current, boolean hidePrevious, boolean isModal) {
        if ((!stageStack.isEmpty()) && hidePrevious){
            Stage prev = stageStack.peek();
            prev.hide();
        }

        stageStack.push(current);

        if (isModal) {
            current.initModality(Modality.APPLICATION_MODAL);
        }

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

    public void showBunkHouseScreen(BunkHouse bunk, int sessionId, TableView<BunkHouse> t) throws Exception {
        log.debug("Showing bunk house form");

        Stage stage = new Stage();
        stage.setTitle("Bunk House");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bunkhouse.fxml"));

        Pane pane = loader.load();
        BunkHouseController controller = loader.getController();
        controller.setTable(bunk, sessionId, t);
        Scene scene = new Scene(pane, 340, 190);
        stage.setScene(scene);

        showNewScreenHidePrevious(stage, false, true);

        stage.show();
    }

    public void showPayScreen(CamperRegistration cr, TableView<CamperRegistration> table) throws Exception {
        log.debug("Showing pay form");

        Stage stage = new Stage();
        stage.setTitle("Payment");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/payments.fxml"));

        Pane pane = loader.load();
        PaymentController controller = loader.getController();
        controller.setCamperRegistration(cr, table);
        Scene scene = new Scene(pane, 242, 150);
        stage.setScene(scene);

        showNewScreenHidePrevious(stage, false, true);

        stage.show();
    }

    public void showApplicationScreen() throws Exception {
        log.debug("Showing application form");
        Stage stage = setStage("Camper Application Form", "/application.fxml", 600, 550);

        showNewScreenHidePrevious(stage);

        stage.show();
    }

    public void showEmergencyContactsScreen() throws Exception {
        log.debug("Emergency Contacts");
        Stage stage = setStage("Emergency Contacts", "/emergencycontacts.fxml", 552, 417);

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
        Stage stage = setStage("Register Campers", "/checkin.fxml", 653, 515);

        showNewScreenHidePrevious(stage);

        stage.show();
    }

    public void showBunkHouseRosterScreen() throws Exception{
        log.debug("Showing Bunkhouse Screen");
        Stage stage = setStage("Bunkhouse", "/bunkhouseroster.fxml", 653, 515);

        showNewScreenHidePrevious(stage);

        stage.show();
    }

    public void showMainMenu(Stage stage) throws Exception {
        log.debug("Showing main menu form");


        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/menu.fxml"));
        Pane myPane = myLoader.load();
        Scene myScene = new Scene(myPane, 350, 475);

        stage.setTitle("Main Menu");
        stage.setScene(myScene);

        showNewScreenHidePrevious(stage);
    }

    public void showVerifyCheckinItems(CamperRegistration cr) throws Exception {
        log.debug("Showing verify check-in items form");

        Stage stage = new Stage();
        stage.setTitle("Check List");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/verifycheckinitems.fxml"));

        Pane pane = loader.load();
        VerifyCheckinItemsController controller = loader.getController();
        controller.setSessionInfo(cr);
        Scene scene = new Scene(pane, 376, 456);
        stage.setScene(scene);

        showNewScreenHidePrevious(stage);

        stage.show();
    }

    public void showTribeRosterScreen() throws Exception { 
        log.debug("Showing Tribe-Roster");
        Stage stage = setStage("Tribe Roster", "/triberoster.fxml", 600, 450);

        showNewScreenHidePrevious(stage);

        stage.show();
    }

    public void showBunkHouseConfig() throws Exception {
        log.debug("Showing Bunk House Configuration");
        Stage stage = setStage("Bunk House Configuration", "/bunkhouseconfig.fxml", 490, 360);

        showNewScreenHidePrevious(stage);

        stage.show();
    }

    public void showBunkHouseChangeAssignmentScreen(BunkHouseAssignment bha, ObservableList<BunkHouseAssignment> bhas,
                                                    TableView<BunkHouseAssignment> bunkhouseTable)
            throws Exception {
        log.debug("Showing Bunk House Swap screen\"");

        Stage stage = new Stage();
        stage.setTitle("Bunk House Swap");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bunkswap.fxml"));

        Pane pane = loader.load();
        BunkHouseSwapController controller = loader.getController();
        controller.setSelectedAssignment(bha, bhas, bunkhouseTable);
        Scene scene = new Scene(pane, 515, 140);
        stage.setScene(scene);

        showNewScreenHidePrevious(stage, false, true);

        stage.show();
    }

    public void showTribeConfig() throws Exception {
        log.debug("Showing Bunk House Configuration");
        Stage stage = setStage("Tribe Configuration", "/tribeconfig.fxml", 490, 360);

        showNewScreenHidePrevious(stage);

        stage.show();
    }

    public void showSessionConfig() throws Exception {
        log.debug("Showing Session Configuration");
        Stage stage = setStage("Camp Session Configuration", "/sessionconfig.fxml", 570, 370);

        showNewScreenHidePrevious(stage);

        stage.show();
    }

    public void showTribeScreen(Tribe tribe, int campSessioId, TableView<Tribe> tribeTableView) throws IOException {
        log.debug("Showing tribe form");

        Stage stage = new Stage();
        stage.setTitle("Tribe");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tribe.fxml"));

        Pane pane = loader.load();
        TribeController controller = loader.getController();
        controller.setTable(tribe, campSessioId, tribeTableView);
        Scene scene = new Scene(pane, 340, 190);
        stage.setScene(scene);

        showNewScreenHidePrevious(stage, false, true);

        stage.show();
    }
}
