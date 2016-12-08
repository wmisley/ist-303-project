package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.UserDAO;
import com.cgu.ist303.project.dao.model.User;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class LoginController {
    private static final Logger log = LogManager.getLogger(LoginController.class);

    public LoginController() {
    }

    public User loginPrompt() throws Exception {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Please provide login and password");

        //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> username.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            } else {
                System.exit(0);
            }

            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        User user = null;

        if (result.isPresent()) {
            String userName = result.get().getKey();
            String userPasswrod = result.get().getValue();
            log.info("User {} logging in", userName);

            UserDAO userDAO = DAOFactory.createUserDAO();
            user = userDAO.query(userName.replaceAll("'", "''"), userPasswrod.replaceAll("'", "''"));
        }

        return user;
    }

    public void displayInvalidCredentials() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText("The login or password you provided is not correct");
        alert.showAndWait();
    }
}
