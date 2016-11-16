package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.TribeAssignmentDAO;
import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import com.cgu.ist303.project.dao.model.Tribe;
import com.cgu.ist303.project.dao.model.TribeAssignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;


public class SqliteTribeAssignmentDAO extends DAOBase implements TribeAssignmentDAO {
    private static final Logger log = LogManager.getLogger(SqliteTribeAssignmentDAO.class);

    public SqliteTribeAssignmentDAO(String dbPath) {
        super(dbPath);
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

        String sql =
                "SELECT T.TRIBE_ID as TRIBE_ID, TRIBE_NAME, C.CAMPER_ID, C.FIRST_NAME, C.AGE " +
                "FROM TRIBES T LEFT OUTER JOIN TRIBE_ASSIGNMENTS TA ON TA.TRIBE_ID = T.TRIBE_ID " +
                "LEFT OUTER JOIN CAMPERS C ON C.CAMPER_ID = TA.CAMPER_ID " +
                "WHERE T.CAMP_SESSION_ID = %d ";

//        String sql = "SELECT * FROM TRIBE_ASSIGNMENTS WHERE CAMPER_ID = 1";
        sql = String.format(sql, sessionId);
        log.debug(sql);
        ResultSet rs = stmt.executeQuery(sql);

        while ( rs.next() ) {
            Camper camper = new Camper();
            camper.setFirstName(rs.getString(4));
            camper.setAge(rs.getInt(5));

            /*
            if (!rs.wasNull()) {
                camper.setCamperId(camperId);
                camper.setFirstName(rs.getString("FIRST_NAME"));
                camper.setMiddleName(rs.getString("MIDDLE_NAME"));
                camper.setLastName(rs.getString("LAST_NAME"));
                camper.setAge(rs.getInt("AGE"));
                camper.setGenderValue(rs.getInt("GENDER"));
            } else {
                camper = null;
            }
            */

            Tribe t = new Tribe();
            t.setTribeId(rs.getInt("TRIBE_ID"));
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
