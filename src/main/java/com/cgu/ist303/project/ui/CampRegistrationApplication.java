package com.cgu.ist303.project.ui;

import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.UserDAO;
import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.model.User;
import com.cgu.ist303.project.dao.sqlite.SqliteDBCreator;
import com.cgu.ist303.project.ui.controllers.LoginController;
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
        LoginController lc = new LoginController();
        DAOFactory.dbPath = "ist303-presentation-2.db";

        File f = new File(DAOFactory.dbPath);

        if (!f.exists()) {
            log.warn("Database does not exist at {}", DAOFactory.dbPath);
            createDb(DAOFactory.dbPath);
        }

        User user = null;

        do {
            user = lc.loginPrompt();

            if (user == null) {
                lc.displayInvalidCredentials();
            }
        } while (user == null);

        LoggedInUser.getInstance().setUser(user);
        UIManager.getInstance().showMainMenu(primaryStage);
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
}
