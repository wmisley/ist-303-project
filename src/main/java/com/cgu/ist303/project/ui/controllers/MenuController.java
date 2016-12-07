package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.model.User;
import com.cgu.ist303.project.ui.LoggedInUser;
import com.cgu.ist303.project.ui.UIManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;


public class MenuController implements Initializable {
    private static final Logger log = LogManager.getLogger(MenuController.class);

    @FXML
    public Button bunkHouseConfig;
    @FXML
    public Button tribeConfig;
    @FXML
    public Button sessionConfig;
    @FXML
    public Button logout;

    public void initialize(URL url, ResourceBundle rb) {
        User u = LoggedInUser.getInstance().getUser();

        bunkHouseConfig.setDisable(u.getType() != User.UserType.Director);
        tribeConfig.setDisable(u.getType() != User.UserType.Director);
        sessionConfig.setDisable(u.getType() != User.UserType.Director);
    }

    public void showApplication() throws Exception {
        log.debug("User pressed \"Application\" button");
        UIManager.getInstance().showApplicationScreen();
    }


    public void showCheckIn() throws Exception {
        log.debug("User pressed \"Check-in\" button");
        UIManager.getInstance().showCheckinScreen();
    }

    public void showTribeRoster() throws Exception {
        log.debug("User pressed \"Tribe\" button");
        UIManager.getInstance().showTribeRosterScreen();
    }

    public void showBunkhouseScreen() throws Exception {
        log.debug("User pressed \"Tribe\" button");
        UIManager.getInstance().showBunkHouseRosterScreen();
    }

    public void showBunkhouseConfigScreen() throws Exception {
        log.debug("User pressed \"Bunk House Configuration\" screen");
        UIManager.getInstance().showBunkHouseConfig();
    }

    public void showTribeConfigScreen() throws Exception {
        log.debug("User pressed \"Tribe Configuration\" screen");
        UIManager.getInstance().showTribeConfig();
    }

    public void showSessionConfigScreen() throws Exception {
        log.debug("User pressed \"Tribe Configuration\" screen");
        UIManager.getInstance().showSessionConfig();
    }

    public void logoutClicked() {

    }
}
