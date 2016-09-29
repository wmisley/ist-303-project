package com.cgu.ist303.project.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class SqliteCamperRegistrationDAO implements CamperRegistrationDAO {
    private static final Logger log = LogManager.getLogger(SqliteCamperRegistrationDAO.class);
    public String dbFilepath = "";

    public SqliteCamperRegistrationDAO(String sqliteFilepath) {
        dbFilepath = sqliteFilepath;
    }

    public void insert(CamperRegistration reg) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql = "INSERT INTO CAMP_REGISTRATION " +
                "(CAMP_SESSION_ID, CAMPER_ID) " +
                "     VALUES " +
                "(%d, %d);";

        sql = String.format(sql,
                reg.getCampSessionId(), reg.getCamperId());

        log.debug(sql);

        stmt.executeUpdate(sql);
        ResultSet rs = stmt.getGeneratedKeys();
        int camperId = rs.getInt(1);
        log.info("Camp Session ID is {}", camperId);

        stmt.close();
        c.commit();
        c.close();
    }
}
