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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CampRegistrationDAOTest {
    private static final Logger log = LogManager.getLogger(CampRegistrationDAOTest.class);

    @ClassRule
    public static final TestResources res = new TestResources();

    @Test
    public void testQuery() {
        CamperRegistrationDAO dao = DAOFactory.createCamperRegistrationDAO();
        assertTrue(dao != null);

        CamperRegistrationRecord cr = new CamperRegistrationRecord();
        cr.setCampSessionId(1);
        cr.setCamperId(1);

        try {
            dao.insert(cr);

            PaymentDAO pDAO = DAOFactory.createPaymentDAO();
            Payment p = new Payment();
            p.setAmount(1000.0);
            p.setCampSessionId(1);
            p.setCamperId(1);
            pDAO.insert(p);

            ObservableList<CamperRegistration> camperList = dao.queryRegisteredCampers(2017);
            assertTrue(camperList != null);
            assertEquals(camperList.size(), 1);
            assertEquals(camperList.get(0).getCamperId(), 1);
            assertEquals(camperList.get(0).getFirstName(), "Will");
        } catch (Exception e) {
            log.error(e);
            assertTrue(false);
        }
    }

    @BeforeClass
    static public void setup() {
        log.info("Testing insert camper record");

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

        try {
            dao.insertCamper(camper);
        } catch (Exception e) {
            log.error(e);
        }
    }
}
