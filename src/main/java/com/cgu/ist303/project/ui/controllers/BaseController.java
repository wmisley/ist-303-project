package com.cgu.ist303.project.ui.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

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

    protected boolean displayConfirmationMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            return true;
        }else{
            return false;
        }
    }

    protected void displayError(Exception e) {
        e.printStackTrace();
        log.error(e);
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setContentText(e.getMessage());
        errorAlert.showAndWait();
    }

    protected void displayNotice(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setHeaderText("");
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void displayError(String error) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setContentText(error);
        errorAlert.showAndWait();
    }
}
