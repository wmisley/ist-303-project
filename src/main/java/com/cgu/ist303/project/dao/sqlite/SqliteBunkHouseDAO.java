package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.BunkHouseDAO;
import com.cgu.ist303.project.dao.model.BunkHouse;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqliteBunkHouseDAO extends DAOBase implements BunkHouseDAO{
    private static final Logger log = LogManager.getLogger(SqliteBunkHouseDAO.class);

    public SqliteBunkHouseDAO(String dbPath) {
        super(dbPath);
    }

    public int insert(BunkHouse bh) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql = "INSERT INTO BUNK_HOUSES " +
                "(BUNK_HOUSE_NAME, GENDER, CAMP_SESSION_ID, MAX_OCCUPANTS) " +
                "     VALUES " +
                "('%s', %d, %d, %d);";

        sql = String.format(sql, bh.getBunkHouseName(), bh.getGender().getValue(),
                bh.getCampSessionId(), bh.getMaxOccupants());

        log.debug(sql);

        stmt.executeUpdate(sql);
        ResultSet rs = stmt.getGeneratedKeys();
        int bunkHouseId = rs.getInt(1);
        log.info("Bunk House ID is {}", bunkHouseId);

        stmt.close();
        c.commit();
        c.close();

        return bunkHouseId;
    }
    public ObservableList<BunkHouse> queryBunkhouses(int year) throws Exception{
        ObservableList<BunkHouse> list = FXCollections.observableArrayList();

        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        return list;
    };
    public ObservableList<BunkHouse> queryBunkhouses(int year, int sessionId) throws Exception{
        ObservableList<BunkHouse> list = FXCollections.observableArrayList();

        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);
        stmt = c.createStatement();



        return list;
    };
}
