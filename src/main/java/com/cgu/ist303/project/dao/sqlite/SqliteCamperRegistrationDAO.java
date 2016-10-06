package com.cgu.ist303.project.dao.sqlite;


import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import com.cgu.ist303.project.dao.CamperRegistrationDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


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

        stmt.close();
        c.commit();
        c.close();
    }

    public int queryGenderCount(int campSessionId, Camper.Gender gender) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        int genderCount = 0;
        String sql =
                "SELECT COUNT(*) AS GENDER_COUNT FROM CAMPERS WHERE CAMPER_ID IN " +
                "(SELECT CAMPER_ID FROM CAMP_REGISTRATION WHERE CAMP_SESSION_ID = %d) " +
                "AND GENDER = %d";

        sql = String.format(sql, campSessionId, gender.getValue());
        log.debug(sql);
        ResultSet rs = stmt.executeQuery(sql);

        while ( rs.next() ) {
            genderCount = rs.getInt("GENDER_COUNT");

        }

        log.debug("Gender count is {}", genderCount);
        rs.close();
        stmt.close();
        c.close();

        return genderCount;
    }

    public boolean queryIsCamperRegisterdForYear(Camper camper, int year) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        int regCount = 0;

        String sql =
                "SELECT COUNT(*) AS REG_COUNT " +
                "FROM CAMPERS C, CAMP_REGISTRATION CR, CAMP_SESSIONS CS " +
                "WHERE C.FIRST_NAME = '%s' " +
                    "AND C.MIDDLE_NAME = '%s' " +
                    "AND C.LAST_NAME =  '%s' " +
                    "AND C.PHONE_NUMBER = '%s' " +
                    "AND C.CAMPER_ID = CR.CAMPER_ID " +
                    "AND CR.CAMP_SESSION_ID = CS.CAMP_SESSION_ID " +
                    "AND CS.CAMP_YEAR = %d ";

        sql = String.format(sql, camper.getFirstName(), camper.getMiddleName(), camper.getLastName(),
                camper.getPhoneNumber(), year);
        log.debug(sql);
        ResultSet rs = stmt.executeQuery(sql);

        while ( rs.next() ) {
            regCount = rs.getInt("REG_COUNT");

        }

        log.debug("Registration count for camper is {}", regCount);
        rs.close();
        stmt.close();
        c.close();

        return (regCount > 0);
    }
}
