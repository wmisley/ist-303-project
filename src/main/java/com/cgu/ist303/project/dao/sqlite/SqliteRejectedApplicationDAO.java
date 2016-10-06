package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.RejectedApplicationsDAO;
import com.cgu.ist303.project.dao.model.RejectedApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SqliteRejectedApplicationDAO implements RejectedApplicationsDAO {
    private static final Logger log = LogManager.getLogger(SqliteRejectedApplicationDAO.class);
    public String dbFilepath = "";

    public SqliteRejectedApplicationDAO(String sqliteFilepath) {
        dbFilepath = sqliteFilepath;
    }

    public void insert(RejectedApplication app) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql = "INSERT INTO REJECTED_APPLICATIONS " +
                "(CAMPER_ID, CAMP_SESSION_ID, REASON_FOR_REJECTION) " +
                "     VALUES " +
                "(%d, %d, %d);";

        sql = String.format(sql, app.getCamperId(), app.getCampSessionId(),
                app.getReason().getValue());

        log.debug(sql);

        stmt.executeUpdate(sql);
        stmt.close();
        c.commit();
        c.close();
    }
}
