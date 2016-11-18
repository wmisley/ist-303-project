package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.TribeAssignmentDAO;
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


public class SqliteTribeAssignmentDAO extends DAOBase implements TribeAssignmentDAO {
    private static final Logger log = LogManager.getLogger(SqliteTribeAssignmentDAO.class);

    public SqliteTribeAssignmentDAO(String dbPath) {
        super(dbPath);
    }

    public void insert(List<TribeAssignmentById> assignments) throws Exception {
        Connection c = null;
        Statement stmt = null;
        Exception ee = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        stmt.getConnection().setAutoCommit(false);

        try {
            for (TribeAssignmentById ta : assignments) {
                String sql = "INSERT INTO TRIBE_ASSIGNMENTS " +
                        "(CAMPER_ID, TRIBE_ID) " +
                        "     VALUES " +
                        "(%d, %d);";

                sql = String.format(sql, ta.getCamperId(), ta.getTribe().getTribeId());
                log.debug(sql);
                stmt.executeUpdate(sql);
            }
        } catch (Exception e) {
            stmt.getConnection().rollback();
            ee = e;
        } finally {
            stmt.getConnection().commit();
            //stmt.getConnection().setAutoCommit(true);

            stmt.close();
            c.commit();
            c.close();

            if (ee != null) {
                throw ee;
            }
        }
    }

    public void delete(List<Tribe> tribes) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql = "DELETE FROM TRIBE_ASSIGNMENTS WHERE TRIBE_ID IN (%s)";

        String tribeIdList = "";

        for (int i = 0; i < tribes.size(); i++) {
            tribeIdList += Integer.toString(tribes.get(i).getTribeId());

            if ((tribes.size() != 1) && (i != (tribes.size() - 1))) {
                tribeIdList += ",";
            }
        }

        sql = String.format(sql, tribeIdList);
        log.debug(sql);

        stmt.executeUpdate(sql);

        stmt.close();
        c.commit();
        c.close();
    }

    @Override
    public void insert(int camperId, int tribeId) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql = "INSERT INTO TRIBE_ASSIGNMENTS " +
                "(CAMPER_ID, TRIBE_ID) " +
                "     VALUES " +
                "(%d, %d);";

        sql = String.format(sql, camperId, tribeId);

        log.debug(sql);

        stmt.executeUpdate(sql);
        ResultSet rs = stmt.getGeneratedKeys();

        stmt.close();
        c.commit();
        c.close();
    }

    @Override
    public List<TribeAssignment> query(int sessionId) throws Exception {
        ObservableList<TribeAssignment> list = FXCollections.observableArrayList();
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String sql = "SELECT C.CAMPER_ID AS CID, T.TRIBE_ID AS TID, * " +
                     "FROM TRIBES T, TRIBE_ASSIGNMENTS TA, CAMPERS C " +
                     "WHERE T.CAMP_SESSION_ID = %d " +
                     "  AND T.TRIBE_ID = TA.TRIBE_ID " +
                     "  AND TA.CAMPER_ID = C.CAMPER_ID";

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


            Tribe t = new Tribe();
            t.setTribeId(rs.getInt("TID"));
            t.setCampSessionId(rs.getInt("CAMP_SESSION_ID"));
            t.setTribeName(rs.getString("TRIBE_NAME"));

            TribeAssignment ta = new TribeAssignment();
            ta.setCamper(camper);
            ta.setTribe(t);

            list.add(ta);
        }

        rs.close();
        stmt.close();
        c.close();

        return list;
    }
}
