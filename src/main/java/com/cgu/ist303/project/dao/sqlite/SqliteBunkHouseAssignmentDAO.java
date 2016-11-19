package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.BunkHouseAssignmentDAO;
import com.cgu.ist303.project.dao.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

/**
 * Created by will4769 on 11/19/16.
 */
public class SqliteBunkHouseAssignmentDAO extends DAOBase implements BunkHouseAssignmentDAO {
    private static final Logger log = LogManager.getLogger(SqliteBunkHouseAssignmentDAO.class);

    public SqliteBunkHouseAssignmentDAO(String dbPath) {
        super(dbPath);
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
