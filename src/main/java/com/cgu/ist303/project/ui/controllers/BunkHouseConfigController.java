package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.BunkHouseDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.model.BunkHouse;
import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.ui.UIManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;


public class BunkHouseConfigController extends BaseController implements Initializable {
    private static final Logger log = LogManager.getLogger(BunkHouseConfigController.class);

    @FXML
    public TableView<BunkHouse> bunksTableView;

    public void initialize(URL url, ResourceBundle rb) {
        bunksTableView.getColumns().clear();
        bunksTableView.setEditable(true);

        TableColumn idCol = new TableColumn("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<BunkHouse, String>("bunkHouseId"));

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<BunkHouse, String>("bunkHouseName"));

        TableColumn genderCol = new TableColumn("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<BunkHouse, String>("gender"));

        TableColumn capacityCol = new TableColumn("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<BunkHouse, String>("maxOccupants"));
        capacityCol.setStyle("-fx-alignment: CENTER_RIGHT;");

        bunksTableView.getColumns().addAll(idCol, nameCol, genderCol, capacityCol);


        BunkHouseDAO dao = DAOFactory.createBunkHouseDAO();

        try {
            ObservableList<BunkHouse> bunks = dao.query(1);
            bunksTableView.setItems(bunks);
            bunksTableView.refresh();
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void backClicked() {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
