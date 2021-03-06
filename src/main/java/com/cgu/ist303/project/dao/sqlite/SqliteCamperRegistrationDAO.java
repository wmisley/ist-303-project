package com.cgu.ist303.project.dao.sqlite;


import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import com.cgu.ist303.project.dao.model.CamperRegistrationRecord;
import com.cgu.ist303.project.dao.CamperRegistrationDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class SqliteCamperRegistrationDAO implements CamperRegistrationDAO {
    private static final Logger log = LogManager.getLogger(SqliteCamperRegistrationDAO.class);
    public static final int ALL_SESSIONS = -1;
    public static final int NO_SESSIONS = 0;
    public String dbFilepath = "";

    public SqliteCamperRegistrationDAO(String sqliteFilepath) {
        dbFilepath = sqliteFilepath;
    }

    public void insert(CamperRegistrationRecord reg) throws Exception {
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

    public void checkInCamper(int camperId, int sessionId) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql = "UPDATE CAMP_REGISTRATION "  +
                     "SET IS_CHECKED_IN = 1 " +
                     "WHERE CAMPER_ID = %d " +
                     "   AND CAMP_SESSION_ID = %d";

        sql = String.format(sql, camperId,  sessionId);
        log.debug(sql);

        stmt.executeUpdate(sql);
        c.commit();

        stmt.close();
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
    public ObservableList<CamperRegistration> queryRegisteredCampers(int year) throws Exception {
        return queryRegisteredCampers(year, ALL_SESSIONS, false);
    }

    public ObservableList<CamperRegistration> queryRegisteredCampers(int year, int campSessionId, boolean isSortByAge) throws Exception {
        ObservableList<CamperRegistration> list = FXCollections.observableArrayList();
        CamperRegistration camper = null;
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String sql =
            "SELECT C.FIRST_NAME, C.MIDDLE_NAME, C.LAST_NAME, C.PHONE_NUMBER, C.AGE, C.GENDER, " +
            " C.CAMPER_ID AS CID, CR.CAMP_SESSION_ID AS CSID, SUM(P.AMOUNT) AS TOTAL_PAYMENT, CR.IS_CHECKED_IN, " +
            " CR.REGISTRATION_DATE " +
            "FROM CAMPERS C, CAMP_REGISTRATION CR, CAMP_SESSIONS CS, PAYMENTS P " +
            "WHERE C.CAMPER_ID = CR.CAMPER_ID " +
                "AND CR.CAMP_SESSION_ID = CS.CAMP_SESSION_ID " +
                "AND  CS.CAMP_YEAR = %d " +
                "AND P.CAMPER_ID = C.CAMPER_ID " +
                "AND P.CAMP_SESSION_ID = CS.CAMP_SESSION_ID ";

        if (campSessionId != ALL_SESSIONS) {
            sql += String.format(" AND CS.CAMP_SESSION_ID = %d ", campSessionId);
        }

        sql += "GROUP BY C.CAMPER_ID";

        if (isSortByAge) {
            sql += " ORDER BY C.AGE";
        }

        sql = String.format(sql, year);
        log.debug(sql);
        ResultSet rs = stmt.executeQuery(sql);

        while ( rs.next() ) {
            camper = new CamperRegistration();
            camper.setCamperId(rs.getInt("CID"));
            camper.setCampSessionId(rs.getInt("CSID"));
            camper.setFirstName(rs.getString("FIRST_NAME"));
            camper.setMiddleName(rs.getString("MIDDLE_NAME"));
            camper.setLastName(rs.getString("LAST_NAME"));
            camper.setPhoneNumber(rs.getString("PHONE_NUMBER"));
            camper.setAge(rs.getInt("AGE"));
            camper.setPayment(rs.getDouble("TOTAL_PAYMENT"));
            camper.setCheckedIn(rs.getInt("IS_CHECKED_IN") == 1);

            int gender = rs.getInt("GENDER");

            if (gender == Camper.Gender.Male.getValue()) {
                camper.setGender(Camper.Gender.Male);
            } else if (gender == Camper.Gender.Female.getValue()) {
                camper.setGender(Camper.Gender.Female);
            } else {
                camper.setGender(Camper.Gender.Unspecified);
            }

            try {
                //Date stored in format 'YYYY-MM-DD'
                String regDateString = rs.getString("REGISTRATION_DATE");
                String[] tokens = regDateString.split("-");
                Calendar cal2 = new GregorianCalendar(
                        Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]) - 1, Integer.parseInt(tokens[2]));
                Date regDate = cal2.getTime();
                camper.setRegistrationDate(regDate);
            } catch (Exception e) {
                log.error(e);
                camper.setRegistrationDate(new Date());
            }

            list.add(camper);
        }

        rs.close();
        stmt.close();
        c.close();

        return list;
    }

    public void delete(int camperId, int sessionId) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql =
                "DELETE FROM CAMP_REGISTRATION WHERE CAMP_SESSION_ID = %d AND CAMPER_ID = %d";

        sql = String.format(sql, sessionId, camperId);

        log.debug(sql);

        stmt.executeUpdate(sql);

        stmt.close();
        c.commit();
        c.close();
    }

    @Override
    public void update(int camperId, int campSessioId, double balance) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        log.debug(balance);
        stmt = c.createStatement();
        String sql =
                "UPDATE PAYMENTS SET AMOUNT = %f WHERE CAMP_SESSION_ID = %d AND CAMPER_ID = %d";

        sql = String.format(sql,balance, campSessioId, camperId);

        log.debug(sql);

        stmt.executeUpdate(sql);

        stmt.close();
        c.commit();
        c.close();
    }
}
