package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.UserDAO;
import com.cgu.ist303.project.dao.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqliteUserDAO implements UserDAO {
    private static final Logger log = LogManager.getLogger(SqliteUserDAO.class);
    public String dbFilepath = "";

    public SqliteUserDAO(String sqliteFilepath) {
        dbFilepath = sqliteFilepath;
    }

    public User query(String login, String password) throws Exception {
        User user = null;
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String sql = "SELECT * " +
                "FROM USERS " +
                "WHERE LOGIN = '%s' " +
                "    AND PASSWORD = '%s' ";

        sql = String.format(sql, login, password);
        ResultSet rs = stmt.executeQuery(sql);

        while ( rs.next() ) {
            user = new User();
            int role = rs.getInt("ROLE");

            if (role == User.UserType.Clerk.getValue()) {
                user.setType(User.UserType.Clerk);
            } else if (role == User.UserType.Director.getValue()) {
                user.setType(User.UserType.Director);
            } else {
                user.setType(User.UserType.Unspecified);
            }

            user.setUser(login);
            user.setPasswrod(password);
        }

        rs.close();
        stmt.close();
        c.close();

        return user;
    }

    public void insert(User user) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql = "INSERT INTO USERS " +
                "(LOGIN, PASSWORD, ROLE) " +
                "     VALUES " +
                "('%s', '%s', %d);";

        sql = String.format(sql, user.getUser(), user.getPasswrod(), user.getType().getValue());

        log.debug(sql);

        stmt.executeUpdate(sql);
        stmt.close();
        c.commit();
        c.close();
    }
}
