package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.BunkHouseDAO;
import com.cgu.ist303.project.dao.model.BunkHouse;
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

public class SqliteBunkHouseDAO extends DAOBase implements BunkHouseDAO{
    private static final Logger log = LogManager.getLogger(SqliteBunkHouseDAO.class);

    public SqliteBunkHouseDAO(String dbPath) {
        super(dbPath);
    }

    public int delete(BunkHouse bh) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql =
                "DELETE FROM BUNK_HOUSES WHERE BUNK_HOUSE_ID = %d";

        sql = String.format(sql, bh.getBunkHouseId());

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

    public int update(BunkHouse bh) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql =
                "UPDATE BUNK_HOUSES " +
                "SET    BUNK_HOUSE_NAME = '%s', GENDER = %d, MAX_OCCUPANTS = %d " +
                "WHERE  BUNK_HOUSE_ID = %d";

        sql = String.format(sql, bh.getBunkHouseName(), bh.getGender().getValue(),
                bh.getMaxOccupants(), bh.getBunkHouseId());

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

    public ObservableList<BunkHouse> query(int campSessionId) throws Exception {
        return query(campSessionId, BunkHouse.Gender.Unspecified);
    }

    public ObservableList<BunkHouse> query(int campSessionId, BunkHouse.Gender genderFilter) throws Exception {
        ObservableList<BunkHouse> list = FXCollections.observableArrayList();
        BunkHouse bh = null;
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String sql = "SELECT * FROM BUNK_HOUSES WHERE CAMP_SESSION_ID = %d ";
        sql = String.format(sql, campSessionId);

        if (genderFilter == BunkHouse.Gender.Female) {
            sql += "AND GENDER = 1 ";
        } else if (genderFilter == BunkHouse.Gender.Male) {
            sql += "AND GENDER = 0 ";
        }

        sql += "ORDER BY BUNK_HOUSE_ID";


        log.debug(sql);
        ResultSet rs = stmt.executeQuery(sql);

        while ( rs.next() ) {
            bh = new BunkHouse();
            bh.setBunkHouseName(rs.getString("BUNK_HOUSE_NAME"));
            bh.setBunkHouseId(rs.getInt("BUNK_HOUSE_ID"));
            bh.setCampSessionId(rs.getInt("CAMP_SESSION_ID"));
            bh.setMaxOccupants(rs.getInt("MAX_OCCUPANTS"));

            int gender = rs.getInt("GENDER");

            if (gender == Camper.Gender.Male.getValue()) {
                bh.setGender(BunkHouse.Gender.Male);
            } else if (gender == Camper.Gender.Female.getValue()) {
                bh.setGender(BunkHouse.Gender.Female);
            } else {
                bh.setGender(BunkHouse.Gender.Unspecified);
            }

            list.add(bh);
        }

        rs.close();
        stmt.close();
        c.close();

        return list;
    }
}
