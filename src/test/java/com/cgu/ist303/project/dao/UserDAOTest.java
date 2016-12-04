package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.User;
import com.cgu.ist303.project.resources.TestResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserDAOTest {
    private static final Logger log = LogManager.getLogger(UserDAOTest.class);

    @ClassRule
    public static final TestResources res = new TestResources();

    @Test
    public void testInsertQueryUser() {
        UserDAO dao = DAOFactory.createUserDAO();
        assertTrue(dao != null);

        try {
            User insertUser = new User();
            insertUser.setPasswrod("user_password");
            User user = dao.query("user_login", "user_password");
            //assertTrue(user != null);

            //assertEquals(user.getPasswrod(), "user_password");
        } catch (Exception e) {
            log.error(e);
            assertTrue(false);
        }
    }
}
