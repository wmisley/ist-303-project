package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.CampSessionDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by will4769 on 9/29/16.
 */
public class SqliteCampSessionDAO implements CampSessionDAO {
    private static final Logger log = LogManager.getLogger(SqliteCampSessionDAO.class);
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
        log.info("Camp Session ID is {}", camperId);

        stmt.close();
        c.commit();
        c.close();

        return camperId;
    }

    public List<CampSession> query(int year) throws Exception {
        Connection c = null;
        Statement stmt = null;
        List<CampSession> sessions = new ArrayList<CampSession>();

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String sql = "SELECT * FROM CAMP_SESSIONS WHERE CAMP_YEAR = %d";
        ResultSet rs = stmt.executeQuery(String.format(sql, year));

        while ( rs.next() ) {
            CampSession session = new CampSession();
            session.setStartMonth(rs.getInt("START_MONTH"));
            session.setStartDay(rs.getInt("START_DAY"));
            session.setEndMonth(rs.getInt("END_MONTH"));
            session.setEndDay(rs.getInt("END_DAY"));
            session.setCampYear(rs.getInt("CAMP_YEAR"));
            session.setGenderLimit(rs.getInt("GENDER_LIMIT"));
            session.setCampSessioId(rs.getInt("CAMP_SESSION_ID"));

            sessions.add(session);
        }

        rs.close();
        stmt.close();
        c.close();

        return sessions;
    }
}
