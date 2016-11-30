package com.cgu.ist303.project.ui.controllers;

import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.TribeDAO;
import com.cgu.ist303.project.dao.model.Tribe;
import com.cgu.ist303.project.ui.UIManager;
import com.cgu.ist303.project.ui.controls.LimitedTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by a on 26-11-2016.
 */
public class TribeController extends BaseController implements Initializable {

    @FXML
    public LimitedTextField name;

    private TableView<Tribe> tribeTable;
    private Tribe tr = null;
    private int sessionId = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setMaxlength(30);
    }

    public void cancelClicked(ActionEvent actionEvent) {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }

    public void okClicked(ActionEvent actionEvent) {
        TribeDAO dao  = DAOFactory.createTribeDAO();

        try {
            if (tr == null) {
                Tribe tribe  = new Tribe();
                tribe.setCampSessionId(sessionId);
                tribe.setTribeName(name.getText());
                int tribeId = dao.insert(tribe);
                tribe.setTribeId(tribeId);

                tribeTable.getItems().add(tribe);
                tribeTable.refresh();

                UIManager.getInstance().closeCurrentScreenShowPrevious();
            } else {
                tr.setTribeName(name.getText());

                dao.update(tr);
                tribeTable.refresh();

                UIManager.getInstance().closeCurrentScreenShowPrevious();
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void setTable(Tribe tribe, int campSessioId, TableView<Tribe> tribeTableView) {
        tribeTable = tribeTableView;
        tr = tribe;
        sessionId = campSessioId;
        if (tribe != null) {
            name.setText(tribe.getTribeName());
        }
    }
}
