package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.BunkHouseDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.model.BunkHouse;
import com.cgu.ist303.project.ui.UIManager;
import com.cgu.ist303.project.ui.controls.LimitedNumberTextField;
import com.cgu.ist303.project.ui.controls.LimitedTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BunkHouseController extends BaseController implements Initializable {
    private static final Logger log = LogManager.getLogger(BunkHouseController.class);

    @FXML
    public LimitedTextField name;
    @FXML
    public ComboBox<String> gender;
    @FXML
    public LimitedNumberTextField capacity;

    private TableView<BunkHouse> bhTable;
    private BunkHouse bh = null;
    private int sessionId = -1;

    public void initialize(URL url, ResourceBundle rb) {
        List<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");

        ObservableList<String> obsGenders = FXCollections.observableList(genders);
        gender.setItems(obsGenders);
        gender.getSelectionModel().select(0);

        name.setMaxlength(30);
        capacity.setMaxlength(3);
    }

    public void setTable(BunkHouse bunk, int sId, TableView<BunkHouse> t) {
        bhTable = t;
        bh = bunk;
        sessionId = sId;

        if (bunk != null) {
            name.setText(bunk.getBunkHouseName());
            capacity.setText(String.valueOf(bunk.getMaxOccupants()));

            if (bunk.getGender() == BunkHouse.Gender.Male) {
                gender.getSelectionModel().select(0);
            } else {
                gender.getSelectionModel().select(1);
            }
        }
    }

    public void okClicked() {
        BunkHouseDAO dao  = DAOFactory.createBunkHouseDAO();
        String g = gender.getSelectionModel().getSelectedItem();
        BunkHouse.Gender gg = g.equalsIgnoreCase("Male") ? BunkHouse.Gender.Male : BunkHouse.Gender.Female;

        try {
            if (bh == null) {
                BunkHouse house = new BunkHouse();
                house.setCampSessionId(sessionId);
                house.setBunkHouseName(name.getText());
                house.setMaxOccupants(Integer.parseInt(capacity.getText()));
                house.setGender(gg);
                int bhId = dao.insert(house);
                house.setBunkHouseId(bhId);

                bhTable.getItems().add(house);
                bhTable.refresh();

                UIManager.getInstance().closeCurrentScreenShowPrevious();
            } else {
                bh.setBunkHouseName(name.getText());
                bh.setMaxOccupants(Integer.parseInt(capacity.getText()));
                bh.setGender(gg);

                dao.update(bh);
                bhTable.refresh();

                UIManager.getInstance().closeCurrentScreenShowPrevious();
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void cancelClicked() {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
