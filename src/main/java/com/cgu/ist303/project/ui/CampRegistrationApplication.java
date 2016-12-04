package com.cgu.ist303.project.ui;

import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.UserDAO;
import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.model.User;
import com.cgu.ist303.project.dao.sqlite.SqliteDBCreator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import static com.cgu.ist303.project.dao.DAOFactory.createUserDAO;

public class CampRegistrationApplication extends Application {
    private static final Logger log = LogManager.getLogger(CampRegistrationApplication.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        DAOFactory.dbPath = "ist303-presentation-2.db";

        File f = new File(DAOFactory.dbPath);

        if (!f.exists()) {
            log.warn("Database does not exist at {}", DAOFactory.dbPath);
            createDb(DAOFactory.dbPath);
        }

        User user = null;

        do {
            user = loginPrompt();

            if (user == null) {
                displayInvalidCredentials();
            }
        } while (user == null);

        LoggedInUser.getInstance().setUser(user);
        UIManager.getInstance().showMainMenu(primaryStage);
    }

    protected void displayInvalidCredentials() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText("The login or password you provided is not correct");
        alert.showAndWait();
    }

    public void createDb(String dbPath) {
        log.info("Creating sqlite3 database at {}", dbPath);

        try {
            SqliteDBCreator db = new SqliteDBCreator();
            db.createDb(dbPath);
        } catch (Exception e) {
            log.error("Unable to create database", e);
        }
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
            user = userDAO.query(userName, userPasswrod);
        }

        return user;
    }
}
