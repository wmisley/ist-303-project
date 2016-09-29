package com.cgu.ist303.project.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 *         String sql = "CREATE TABLE CAMP_SESSIONS " +
 "(CAMP_SESSION_ID   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
 " CAMP_YEAR           INTEGER NOT NULL, " +
 " START_MONTH          INTEGER NOT NULL, " +
 " START_DAY            INTEGER NOT NULL, " +
 " END_MONTH            INTEGER NOT NULL, " +
 " END_DAY            INTEGER NOT NULL, " +
 " GENDER_LIMIT        INTEGER NOT NULL, " +
 " CONSTRAINT CAMPER_SESSION_UNIQUE_KEY UNIQUE (CAMP_YEAR, START_DATE, END_DATE))";
 */


public class SqliteCamperRegistrationDAO {
    public SqliteCamperRegistrationDAO() {
    }
}
