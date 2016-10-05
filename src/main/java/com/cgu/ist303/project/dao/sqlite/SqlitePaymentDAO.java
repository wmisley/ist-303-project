package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.PaymentDAO;
import com.cgu.ist303.project.dao.model.Payment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SqlitePaymentDAO implements PaymentDAO {
    private static final Logger log = LogManager.getLogger(PaymentDAO.class);
    public String dbFilepath = "";

    public SqlitePaymentDAO(String sqliteFilepath) {
        dbFilepath = sqliteFilepath;
    }

    public void insert(Payment payment) throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        String sql = "INSERT INTO PAYMENTS " +
                "(CAMPER_ID, CAMP_SESSION_ID, AMOUNT, IS_CLEARED) " +
                "     VALUES " +
                "(%d, %d, %f, 0);";

        sql = String.format(sql, payment.getCamperId(), payment.getCampSessionId(),
            payment.getAmount());

        log.debug(sql);

        stmt.executeUpdate(sql);
        stmt.close();
        c.commit();
        c.close();
    }
}
