package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.CamperDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqliteCamperDAO implements CamperDAO {
    private static final Logger log = LogManager.getLogger(SqliteCamperDAO.class);
    public String dbFilepath = "";

    public SqliteCamperDAO(String sqliteFilepath) {
        dbFilepath = sqliteFilepath;
    }

    public int insertCamper(Camper camper) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql = "INSERT INTO CAMPERS " +
                     "(FIRST_NAME, MIDDLE_NAME, LAST_NAME, GENDER, AGE, STREET_NUMBER, STREET, APT_NUMBER, " +
                     " CITY, STATE, ZIPCODE, PHONE_NUMBER, RP_FIRST_NAME, RP_MIDDLE_NAME, RP_LAST_NAME) " +
                     "     VALUES " +
                     "('%s', '%s', '%s', %d, %d, '%s','%s', '%s'," +
                     " '%s', '%s', '%s', '%s', '%s', '%s', '%s');";

        sql = String.format(sql,
                camper.getFirstName(), camper.getMiddleName(), camper.getLastName(),
                camper.getGender() == Camper.Gender.Male ? 1 : 0, camper.getAge(), camper.getStreetNumber(),
                camper.getStreet(), camper.getAptNumber(), camper.getCity(), camper.getState(),
                camper.getZipCode(), camper.getPhoneNumber(), camper.getRpFirstName(), camper.getRpMiddleName(),
                camper.getRpLastName());

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

