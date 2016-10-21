package com.cgu.ist303.project.ui.controllers;


import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.PaymentDAO;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import com.cgu.ist303.project.dao.model.Payment;
import com.cgu.ist303.project.ui.UIManager;
import com.cgu.ist303.project.ui.controls.LimitedNumberTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentController extends BaseController implements Initializable{
    private static final Logger log = LogManager.getLogger(PaymentController.class);

    private CamperRegistration camperRegistration = null;
    private TableView<CamperRegistration> crTable = null;

    @FXML
    private Button ok;
    @FXML
    private Button cancel;
    @FXML
    private LimitedNumberTextField payment;

    public void initialize(URL url, ResourceBundle rb) {
    }

    public void saveClicked() {
        if (payment.getText().length() <= 0) {
            displayAlertMessage("No payment amount was entered.");
        } else if (Double.parseDouble(payment.getText()) <= 0) {
            displayAlertMessage("A positive dollar amount must be entered.");
        } else {
            double amount = Double.parseDouble(payment.getText());

            PaymentDAO dao = DAOFactory.createPaymentDAO();
            Payment pay = new Payment();
            pay.setCamperId(camperRegistration.getCamperId());
            pay.setCampSessionId(camperRegistration.getCampSessionId());
            pay.setAmount(amount);

            log.debug("Inserting payment record");

            try {
                dao.insert(pay);
                camperRegistration.setPayment(camperRegistration.getPayment() + amount);
            } catch (Exception e) {
                log.error("Could not save payment", e);
                displayErrorMessage("Could not save payment", e);
            }

            this.crTable.refresh();
            UIManager.getInstance().closeCurrentScreenShowPrevious();

        }
    }

    public void setCamperRegistration(CamperRegistration cr, TableView<CamperRegistration> table) {
        camperRegistration = cr;
        this.crTable = table;
    }

    public void cancelClicked() {
        UIManager.getInstance().closeCurrentScreenShowPrevious();
    }
}
