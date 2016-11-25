package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import com.cgu.ist303.project.dao.model.CamperRegistrationRecord;
import com.cgu.ist303.project.dao.model.Payment;
import com.cgu.ist303.project.resources.TestResources;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PaymentDAOTest {
    private static final Logger log = LogManager.getLogger(PaymentDAOTest.class);

    @ClassRule
    public static final TestResources res = new TestResources();

    @Test
    public void testInsert() {
        CamperRegistrationDAO crDAO = DAOFactory.createCamperRegistrationDAO();

        try {
            PaymentDAO pDAO = DAOFactory.createPaymentDAO();
            Payment p = new Payment();
            p.setAmount(1000.0);
            p.setCampSessionId(1);
            p.setCamperId(1);
            pDAO.insert(p);

            ObservableList<CamperRegistration> camperList = crDAO.queryRegisteredCampers(2017);
            assertTrue(camperList != null);
            assertEquals(camperList.size(), 1);
            assertEquals(camperList.get(0).getCamperId(), 1);
            assertTrue(((int) camperList.get(0).getPayment() - 1000.0) == 0);
        } catch (Exception e) {
            log.error(e);
            assertTrue(false);
        }
    }

    @BeforeClass
    static public void setup() {
        log.info("Inserting a camper record");

        DAOFactory.dbPath = TestResources.dbFile;
        CamperDAO dao = DAOFactory.createCamperDAO();

        Camper camper = new Camper();
        camper.setFirstName("Will");
        camper.setMiddleName("M");
        camper.setLastName("Isley");
        camper.setStreet("123 State St.");
        camper.setState("CA");
        camper.setZipCode("88888");
        camper.setGender(Camper.Gender.Male);
        camper.setAptNumber("456");
        camper.setAge(29);
        camper.setPhoneNumber("5553332222");
        camper.setRpFirstName("Sam");
        camper.setRpMiddleName("N");
        camper.setRpLastName("Theman");

        CamperRegistrationDAO crDAO = DAOFactory.createCamperRegistrationDAO();

        CamperRegistrationRecord cr = new CamperRegistrationRecord();
        cr.setCampSessionId(1);
        cr.setCamperId(1);

        try {
            dao.insertCamper(camper);
            crDAO.insert(cr);
        } catch (Exception e) {
            log.error(e);
        }
    }
}
