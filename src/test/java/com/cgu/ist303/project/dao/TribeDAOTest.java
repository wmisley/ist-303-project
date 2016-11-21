package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.Tribe;
import com.cgu.ist303.project.resources.TestResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by will4769 on 11/20/16.
 */
public class TribeDAOTest {
    private static final Logger log = LogManager.getLogger(TribeDAOTest.class);

    @ClassRule
    public static final TestResources res = new TestResources();

    @Test
    public void testInsertQueryTribes() {
        TribeDAO dao = DAOFactory.createTribeDAO();
        assertTrue(dao != null);

        try {
            List<Tribe> tribes = dao.query(1);
            assertTrue(tribes != null);

            assertEquals(tribes.size(), 4);
        } catch (Exception e) {
            log.error(e);
            assertTrue(false);
        }
    }
}
