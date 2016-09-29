package com.cgu.ist303.project.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by will4769 on 9/29/16.
 */
public class SqliteCampSessionDAO {
    private static final Logger log = LogManager.getLogger(SqliteDBCreator.class);
    public String dbFilepath = "";

    public SqliteCampSessionDAO(String sqliteFilepath) {
        dbFilepath = sqliteFilepath;
    }

    public int insert(CampSession session) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql = "INSERT INTO CAMP_SESSIONS " +
                "(CAMP_YEAR, START_MONTH, START_DAY, END_MONTH, END_DAY, GENDER_LIMIT) " +
                "     VALUES " +
                "(%d, %d, %d, %d, %d, %d);";

        sql = String.format(sql,
                session.getCampYear(), session.getStartMonth(), session.getStartDay(),
                session.getEndMonth(), session.getEndDay(), session.getGenderLimit());

        log.debug(sql);

        stmt.executeUpdate(sql);
        ResultSet rs = stmt.getGeneratedKeys();
        int camperId = rs.getInt(1);
        log.info("Camper ID is {}", camperId);

        stmt.close();
        c.commit();
        c.close();

        return camperId;
    }
}
