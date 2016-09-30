package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.TribeDAO;
import com.cgu.ist303.project.dao.model.Tribe;
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
                "(TRIBE_NAME, CAMP_YEAR) " +
                "     VALUES " +
                "('%s', %d);";

        sql = String.format(sql, tribe.getTribeName(), tribe.campYear);

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
