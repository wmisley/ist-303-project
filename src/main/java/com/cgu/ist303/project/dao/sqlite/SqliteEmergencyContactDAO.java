package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.EmergencyContact;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class SqliteEmergencyContactDAO extends DAOBase implements EmergencyContactDAO {
    private static final Logger log = LogManager.getLogger(SqliteEmergencyContactDAO.class);

    public SqliteEmergencyContactDAO(String dbPath) {
        super(dbPath);
    }

    public int insertCamper(EmergencyContact ec) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql = "INSERT INTO EMERGENCY_CONTACTS " +
                "(CAMPER_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, PRIMARY_PHONE, ALTERNATE_PHONE) " +
                "     VALUES " +
                "(%d, '%s', '%s', '%s','%s', '%s');";

        sql = String.format(sql,
                ec.getCamperId(), ec.getFirstName(), ec.getMiddleName(), ec.getLastName(),
                ec.getPhone1(), ec.getPhone2());

        log.debug(sql);

        stmt.executeUpdate(sql);
        ResultSet rs = stmt.getGeneratedKeys();
        int ecId = rs.getInt(1);
        log.info("Emergency Contact ID is {}", ecId);

        stmt.close();
        c.commit();
        c.close();

        return ecId;
    }
}
