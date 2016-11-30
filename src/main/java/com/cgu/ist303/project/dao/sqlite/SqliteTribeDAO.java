package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.TribeDAO;
import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import com.cgu.ist303.project.dao.model.Tribe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqliteTribeDAO implements TribeDAO {
    private static final Logger log = LogManager.getLogger(SqliteTribeDAO.class);
    public String dbFilepath = "";

    public SqliteTribeDAO(String sqliteFilepath) {
        dbFilepath = sqliteFilepath;
    }

    public int insert(Tribe tribe) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql = "INSERT INTO TRIBES " +
                "(TRIBE_NAME, CAMP_SESSION_ID) " +
                "     VALUES " +
                "('%s', %d);";

        sql = String.format(sql, tribe.getTribeName(), tribe.getCampSessionId());

        log.debug(sql);

        stmt.executeUpdate(sql);
        ResultSet rs = stmt.getGeneratedKeys();
        int tribeId = rs.getInt(1);
        log.info("Tribe ID is {}", tribeId);

        stmt.close();
        c.commit();
        c.close();

        return tribeId;
    }

    public ObservableList<Tribe> query(int campSessionId) throws Exception {
        ObservableList<Tribe> list = FXCollections.observableArrayList();
        Tribe tribe = null;
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String sql =
                "SELECT * FROM TRIBES WHERE CAMP_SESSION_ID = %d ORDER BY TRIBE_ID";

        sql = String.format(sql, campSessionId);
        log.debug(sql);
        ResultSet rs = stmt.executeQuery(sql);

        while ( rs.next() ) {
            tribe = new Tribe();
            tribe.setTribeName(rs.getString("TRIBE_NAME"));
            tribe.setTribeId(rs.getInt("TRIBE_ID"));
            tribe.setCampSessionId(rs.getInt("CAMP_SESSION_ID"));

            list.add(tribe);
        }

        rs.close();
        stmt.close();
        c.close();

        return list;
    }

    @Override
    public int update(Tribe tribe) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql =
                "UPDATE TRIBES " +
                        "SET    TRIBE_NAME = '%s' " +
                        "WHERE  TRIBE_ID = %d";

        sql = String.format(sql, tribe.getTribeName(), tribe.getTribeId());

        log.debug(sql);

        stmt.executeUpdate(sql);
        ResultSet rs = stmt.getGeneratedKeys();
        int tribeId = rs.getInt(1);
        log.info("Tribe ID is {}", tribeId);

        stmt.close();
        c.commit();
        c.close();

        return tribeId;
    }

    public int delete(Tribe t) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql =
                "DELETE FROM TRIBES WHERE TRIBE_ID = %d";

        sql = String.format(sql, t.getTribeId());

        log.debug(sql);

        stmt.executeUpdate(sql);
        ResultSet rs = stmt.getGeneratedKeys();
        int tribeId = rs.getInt(1);
        log.info("Tribe ID is {}", tribeId);

        stmt.close();
        c.commit();
        c.close();

        return tribeId;
    }
}
