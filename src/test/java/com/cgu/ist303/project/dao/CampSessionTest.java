package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.resources.TestResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CampSessionTest {
    private static final Logger log = LogManager.getLogger(CampSessionTest.class);

    @ClassRule
    public static final TestResources res = new TestResources();

    @Test
    public void testQuery() {
        log.info("Testing camp session query");
        CampSessionDAO dao = DAOFactory.createCampSessionDAO();
        assertTrue(dao != null);

        try {
            List<CampSession> sessions = dao.query(2017);
            assertTrue(sessions != null);

            assertEquals(sessions.size(), 3);
        } catch (Exception e) {
            log.error(e);
            assertTrue(false);
        }
    }
}
