package com.cgu.ist303.project.ui;

import com.cgu.ist303.project.dao.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoggedInUser {
    private static final Logger log = LogManager.getLogger(LoggedInUser.class);
    static private LoggedInUser loggedInUser = null;

    private User user = null;

    private LoggedInUser() {
    }

    static public LoggedInUser getInstance() {
        if (loggedInUser == null) {
            loggedInUser = new LoggedInUser();
        }

        return loggedInUser;
    }

    public void setUser(User u) {
        user = u;
    }

    public User getUser() {
        return user;
    }
}
