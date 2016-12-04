package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.resources.TestResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CamperDAOTest {
    private static final Logger log = LogManager.getLogger(CamperDAOTest.class);

    @ClassRule
    public static final TestResources res = new TestResources();

    @Test
    public void testInsertAndQuery() {

        log.info("Testing insert and query for campers");

        DAOFactory.dbPath = TestResources.dbFile;
        CamperDAO dao = DAOFactory.createCamperDAO();

        assertTrue(dao != null);

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
            int camperId = dao.insertCamper(camper);

            assertTrue(camperId >= 0);

            int returnedCamperId = dao.queryCamperId(camper);

            assertEquals(camperId, returnedCamperId);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
