package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.CamperRegistrationDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.ui.UIManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;


public class CheckinController implements Initializable {
    private static final Logger log = LogManager.getLogger(CheckinController.class);

    @FXML
    private TableView<Camper> campersTable;

    public void initialize(URL url, ResourceBundle rb) {
        campersTable.getColumns().clear();
        campersTable.setEditable(true);

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Camper, String>("firstName"));
        TableColumn middleNameCol = new TableColumn("Middle Name");
        middleNameCol.setCellValueFactory(new PropertyValueFactory<Camper, String>("middleName"));
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Camper, String>("lastName"));
        TableColumn phoneNumber = new TableColumn("Phone Number");
        phoneNumber.setCellValueFactory(new PropertyValueFactory<Camper, String>("phoneNumber"));

        campersTable.getColumns().addAll(firstNameCol, middleNameCol, lastNameCol, phoneNumber);

        CamperRegistrationDAO regDAO = DAOFactory.createCamperRegistrationDAO();

        ObservableList<Camper> obsList  = null;

        try {
            obsList = regDAO.queryRegisteredCampers(2016);
            campersTable.setItems(obsList);
        } catch (Exception e) {
            log.error(e);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }

    public void cancelClicked() throws Exception {
        UIManager.getInstance().showMainMenu();
    }
}
