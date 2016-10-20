package com.cgu.ist303.project.ui.controllers;

import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseController {
    private static final Logger log = LogManager.getLogger(BaseController.class);

    protected void displayErrorMessage(String message, Exception e) {
        log.error(message, e);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText("Unhandled error: " + e.getMessage());
        alert.showAndWait();
    }

    protected void displayAlertMessage(String message) {
        log.debug("Display alert message to user: {}", message);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
