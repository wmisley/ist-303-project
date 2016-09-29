package com.cgu.ist303.project.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class SqliteDBCreator {
    private static final Logger log = LogManager.getLogger(SqliteDBCreator.class);

    static public void main(String[] args) {
        final String dbPath = "ist303.db";

        log.info("Creating sqlite3 database at {}", dbPath);

        SqliteDBCreator db = new SqliteDBCreator();
        db.deleteDabaseFile(dbPath);
        db.createTables(dbPath);

        ArrayList<CampSession> sessions = db.createCampSessions(dbPath);
        db.insertCamper(dbPath);
    }

    public ArrayList<CampSession> createCampSessions(String dbPath) {
        ArrayList<CampSession> sessions = new ArrayList<CampSession>();
        DAOFactor.dbPath = dbPath;
        CampSessionDAO sessionDAO = DAOFactor.createCampSessionDAO();

        //TODO: Camps are in the 2nd and 3rd week of the month
        //TODO: Need to verify starts on Sunday, ends on Saturday
        CampSession session = new CampSession();
        session.setCampYear(2016); session.setGenderLimit(36);
        session.setStartMonth(6);  session.setStartDay(5);
        session.setEndMonth(6);    session.setEndDay(18);

        try {
            int sessionId = sessionDAO.insert(session);
            session.setCampSessioId(sessionId);
            sessions.add(session);
        } catch (Exception e) { log.error(e); }

        session = new CampSession();
        session.setCampYear(2016); session.setGenderLimit(36);
        session.setStartMonth(7);  session.setStartDay(3);
        session.setEndMonth(7);    session.setEndDay(16);

        try {
            int sessionId = sessionDAO.insert(session);
            session.setCampSessioId(sessionId);
            sessions.add(session);
        } catch (Exception e) { log.error(e); }

        session = new CampSession();
        session.setCampYear(2016); session.setGenderLimit(36);
        session.setStartMonth(8);  session.setStartDay(7);
        session.setEndMonth(8);    session.setEndDay(20);

        try {
            int sessionId = sessionDAO.insert(session);
            session.setCampSessioId(sessionId);
            sessions.add(session);
        } catch (Exception e) { log.error(e); }

        return sessions;
    }

    public void insertCamper(String dbPath) {
        DAOFactor.dbPath = dbPath;
        CamperDAO camperDAO = DAOFactor.createCamperDAO();

        Camper camper = new Camper();
        camper.setFirstName("William");
        camper.setMiddleName("Michael");
        camper.setLastName("Isley");
        camper.setAge(9);
        camper.setGender(Camper.Gender.Male);
        camper.setAptNumber("");
        camper.setStreetNumber("380");
        camper.setStreet("New York St.");
        camper.setCity("Redlands");
        camper.setState("CA");
        camper.setZipCode("91791");
        camper.setRpFirstName("Bobby");
        camper.setFirstName("");
        camper.setRpLastName("Boon");

        try {
            log.debug("Inserting camper record");
            camperDAO.insertCamper(camper);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void deleteDabaseFile(String filepath) {
        File file = new File(filepath);
        file.delete();
    }


    public void createTables(String dbPath)  {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            log.debug("Opened database successfully");

            createCampersTable(c);
            createCamperSessionsTable(c);
            createCampRegistrationTable(c);
            createUsersTable(c);
            createYearConfigTable(c);
            createPaymentsTable(c);
            createRejectedApplicationsTable(c);
            createTribesTable(c);
            createTribeAssignmentsTable(c);
            createBunkHouseTable(c);
            createBunkHouseCountTable(c);
            createBunkAssignmentsTable(c);

            c.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private void createCampersTable(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE CAMPERS " +
                "(CAMPER_ID        INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                " FIRST_NAME       TEXT     NOT NULL, " +
                " MIDDLE_NAME      TEXT     NULL, " +
                " LAST_NAME        TEXT     NOT NULL, " +
                " GENDER           INTEGER  NOT NULL, " +
                " AGE              INTEGER  NOT NULL, " +
                " STREET_NUMBER    TEXT     NOT NULL, " +
                " STREET           TEXT     NOT NULL, " +
                " APT_NUMBER       TEXT     NOT NULL, " +
                " CITY             TEXT     NOT NULL, " +
                " STATE            TEXT     NOT NULL, " +
                " ZIPCODE          TEXT     NOT NULL, " +
                " PHONE_NUMBER     TEXT     NOT NULL, " +
                " RP_FIRST_NAME    TEXT     NOT NULL, " +
                " RP_MIDDLE_NAME   TEXT     NULL, " +
                " RP_LAST_NAME     TEXT     NOT NULL)";
        log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();
    }

    private void createCamperSessionsTable(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE CAMP_SESSIONS " +
                "(CAMP_SESSION_ID   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                " CAMP_YEAR           INTEGER NOT NULL, " +
                " START_MONTH          INTEGER NOT NULL, " +
                " START_DAY            INTEGER NOT NULL, " +
                " END_MONTH            INTEGER NOT NULL, " +
                " END_DAY            INTEGER NOT NULL, " +
                " GENDER_LIMIT        INTEGER NOT NULL, " +
                " CONSTRAINT CAMPER_SESSION_UNIQUE_KEY UNIQUE (CAMP_YEAR, START_MONTH, START_DAY))";
        log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();
    }

    private void createCampRegistrationTable(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE CAMP_REGISTRATION " +
                "(CAMP_SESSION_ID   INTEGER NOT NULL," +
                " CAMPER_ID         INTEGER NOT NULL, " +
                " CONSTRAINT CAMPER_SESSION_PK PRIMARY KEY (CAMP_SESSION_ID, CAMPER_ID), " +
                " FOREIGN KEY(CAMP_SESSION_ID) REFERENCES CAMP_SESSIONS(CAMP_SESSION_ID), " +
                " FOREIGN KEY(CAMPER_ID) REFERENCES CAMPERS(CAMPER_ID)) ";
        log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();
    }

    private void createUsersTable(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE USERS " +
                "(LOGIN     TEXT NOT NULL PRIMARY KEY, " +
                " PASSWORD  TEXT NOT NULL, " +
                " ROLE      INTEGER NOT NULL)";
        log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();
    }

    private void createYearConfigTable(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE CAMP_YEAR_CONFIG " +
                "(CAMP_YEAR             INTEGER NOT NULL PRIMARY KEY, " +
                " SESSION_COST          REAL NOT NULL, " +
                " NUMBER_OF_BUNK_HOUSES INTEGER NOT NULL, " +
                " NUMBER_OF_TRIBES      INTEGER NOT NULL) ";
        log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();
    }

    private void createPaymentsTable(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE PAYMENTS " +
                "(CAMPER_ID        INTEGER NOT NULL," +
                " CAMP_SESSION_ID  INTEGER NOT NULL, " +
                " AMOUNT           REAL NOT NULL," +
                " IS_CLEARED       INTEGER NOT NULL, " +
                " FOREIGN KEY(CAMP_SESSION_ID) REFERENCES CAMP_SESSIONS(CAMP_SESSION_ID)," +
                " FOREIGN KEY(CAMPER_ID) REFERENCES CAMPERS(CAMPER_ID))";
        log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();
    }

    private void createRejectedApplicationsTable(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE REJECTED_APPLICATIONS " +
                "(CAMPER_ID            INTEGER NOT NULL," +
                " CAMP_SESSION_ID      INTEGER NOT NULL, " +
                " REASON_FOR_REJECTION INTEGER NOT NULL," +
                " FOREIGN KEY(CAMP_SESSION_ID) REFERENCES CAMP_SESSIONS(CAMP_SESSION_ID)," +
                " FOREIGN KEY(CAMPER_ID) REFERENCES CAMPERS(CAMPER_ID))";
        log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();
    }

    private void createTribesTable(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE TRIBES " +
                "(TRIBE_ID    INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                " TRIBE_NAME  TEXT NOT NULL, " +
                " CAMP_YEAR   INTEGER NOT NULL, " +
                " CONSTRAINT TRIBES_UNIQUE UNIQUE (TRIBE_NAME, CAMP_YEAR))";
        log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();
    }

    private void createTribeAssignmentsTable(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE TRIBE_ASSIGNMENTS " +
                "(CAMPER_ID        INTEGER NOT NULL," +
                " CAMP_SESSION_ID  INTEGER NOT NULL, " +
                " TRIBE_ID         INTEGER NOT NULL, " +
                " CONSTRAINT TRIBE_ASSIGNMENTS_PK PRIMARY KEY (CAMP_SESSION_ID, CAMPER_ID, TRIBE_ID)," +
                " FOREIGN KEY(CAMP_SESSION_ID) REFERENCES CAMP_SESSIONS(CAMP_SESSION_ID)," +
                " FOREIGN KEY(TRIBE_ID) REFERENCES TRIBES(TRIBE_ID)," +
                " FOREIGN KEY(CAMPER_ID) REFERENCES CAMPERS(CAMPER_ID))";
        log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();
    }

    private void createBunkHouseTable(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE BUNK_HOUSES " +
                "(BUNK_HOUSE_ID    INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " BUNK_HOUSE_NAME  TEXT NOT NULL UNIQUE)";
        log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();
    }

    private void createBunkHouseCountTable(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE BUNK_HOUSE_COUNTS " +
                "(BUNK_HOUSE_ID    INTEGER NOT NULL, " +
                " CAMP_YEAR        INTEGER NOT NULL, " +
                " BUNK_HOUSE_COUNT INTEGER NOT NULL, " +
                " CONSTRAINT BUNK_HOUSE_COUNTS_PK PRIMARY KEY (BUNK_HOUSE_ID, CAMP_YEAR) " +
                " FOREIGN KEY(BUNK_HOUSE_ID) REFERENCES BUNK_HOUSES(BUNK_HOUSE_ID))";
        log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();
    }

    private void createBunkAssignmentsTable(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE BUNK_HOUSE_ASSIGNMENTS " +
                "(BUNK_HOUSE_ID    INTEGER NOT NULL, " +
                " CAMPER_ID        INTEGER NOT NULL, " +
                " CAMP_SESSION_ID  INTEGER NOT NULL, " +
                " CONSTRAINT BUNK_HOUSE_ASSIGNMENTS_PK PRIMARY KEY (BUNK_HOUSE_ID, CAMPER_ID, CAMP_SESSION_ID), " +
                " FOREIGN KEY(BUNK_HOUSE_ID) REFERENCES BUNK_HOUSES(BUNK_HOUSE_ID), " +
                " FOREIGN KEY(CAMP_SESSION_ID) REFERENCES CAMP_SESSIONS(CAMP_SESSION_ID), " +
                " FOREIGN KEY(CAMPER_ID) REFERENCES CAMPERS(CAMPER_ID))";
        log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();
    }
}


