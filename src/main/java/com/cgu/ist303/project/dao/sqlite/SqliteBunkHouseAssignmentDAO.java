package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.BunkHouseAssignmentDAO;
import com.cgu.ist303.project.dao.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;


public class SqliteBunkHouseAssignmentDAO extends DAOBase implements BunkHouseAssignmentDAO {
    private static final Logger log = LogManager.getLogger(SqliteBunkHouseAssignmentDAO.class);

    public SqliteBunkHouseAssignmentDAO(String dbPath) {
        super(dbPath);
    }

    @Override
    public ObservableList<BunkHouseAssignment> query(int sessionId) throws Exception {
        ObservableList<BunkHouseAssignment> list = FXCollections.observableArrayList();
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String sql = "SELECT C.CAMPER_ID AS CID, BA.BUNK_HOUSE_ID AS BHID, * " +
                "FROM BUNK_HOUSES B, BUNK_HOUSE_ASSIGNMENTS BA, CAMPERS C " +
                "WHERE B.CAMP_SESSION_ID = %d " +
                "  AND B.BUNK_HOUSE_ID = BA.BUNK_HOUSE_ID " +
                "  AND BA.CAMPER_ID = C.CAMPER_ID";

        sql = String.format(sql, sessionId);
        log.debug(sql);
        ResultSet rs = stmt.executeQuery(sql);

        while ( rs.next() ) {
            Camper camper = new CamperRegistration();

            int camperId = rs.getInt("CID");
            camper.setCamperId(camperId);
            camper.setFirstName(rs.getString("FIRST_NAME"));
            camper.setMiddleName(rs.getString("MIDDLE_NAME"));
            camper.setLastName(rs.getString("LAST_NAME"));
            camper.setAge(rs.getInt("AGE"));
            camper.setGenderValue(rs.getInt("GENDER"));


            BunkHouse bh = new BunkHouse();
            bh.setBunkHouseId(rs.getInt("BHID"));
            bh.setCampSessionId(rs.getInt("CAMP_SESSION_ID"));
            bh.setBunkHouseName(rs.getString("BUNK_HOUSE_NAME"));
            bh.setMaxOccupants(rs.getInt("MAX_OCCUPANTS"));

            BunkHouseAssignment bha = new BunkHouseAssignment();
            bha.setCamper(camper);
            bha.setBunkHouse(bh);

            list.add(bha);
        }

        rs.close();
        stmt.close();
        c.close();

        return list;
    }

    public void insert(List<BunkHouseAssignmentById> assignments) throws Exception {

        Connection c = null;
        Statement stmt = null;
        Exception ee = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        stmt.getConnection().setAutoCommit(false);

        try {
            for (BunkHouseAssignmentById ba : assignments) {
                String sql = "INSERT INTO BUNK_HOUSE_ASSIGNMENTS " +
                        "(CAMPER_ID, BUNK_HOUSE_ID) " +
                        "     VALUES " +
                        "(%d, %d);";

                sql = String.format(sql, ba.getCamperId(), ba.getBunkHouse().getBunkHouseId());
                log.debug(sql);
                stmt.executeUpdate(sql);
            }
        } catch (Exception e) {
            stmt.getConnection().rollback();
            ee = e;
        } finally {
            stmt.getConnection().commit();

            stmt.close();
            c.commit();
            c.close();

            if (ee != null) {
                throw ee;
            }
        }
    }

    public void delete(List<BunkHouse> bhList) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql = "DELETE FROM BUNK_HOUSE_ASSIGNMENTS WHERE BUNK_HOUSE_ID IN (%s)";

        String bhIdList = "";

        for (int i = 0; i < bhList.size(); i++) {
            bhIdList += Integer.toString(bhList.get(i).getBunkHouseId());

            if ((bhList.size() != 1) && (i != (bhList.size() - 1))) {
                bhIdList += ",";
            }
        }

        sql = String.format(sql, bhIdList);
        log.debug(sql);

        stmt.executeUpdate(sql);

        stmt.close();
        c.commit();
        c.close();
    }
}
