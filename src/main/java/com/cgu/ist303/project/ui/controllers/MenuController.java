package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.ui.UIManager;
import javafx.fxml.Initializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;


public class MenuController implements Initializable {
    private static final Logger log = LogManager.getLogger(MenuController.class);

    public void initialize(URL url, ResourceBundle rb) {
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
}
