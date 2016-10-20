package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.ArrivalPacketItemDAO;
import com.cgu.ist303.project.dao.model.ArrivalPacketItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by will4769 on 10/19/16.
 */
public class SqliteArrivalPacketItemDAO implements ArrivalPacketItemDAO {
    public String dbFilepath = "";

    public SqliteArrivalPacketItemDAO(String sqliteFilepath) {
        dbFilepath = sqliteFilepath;
    }

    public List<ArrivalPacketItem> query(int year) throws Exception {
        List<ArrivalPacketItem> packetItems = new ArrayList<>();

        /*
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String sql = "SELECT CAMPER_YEAR_PACKET_ITEMS " +
                "FROM CAMPER_YEAR_PACKET_ITEMS " +
                "WHERE YEAR ";

        sql = String.format(sql, camper.getFirstName(), camper.getMiddleName(),
                camper.getLastName(), camper.getPhoneNumber());
        ResultSet rs = stmt.executeQuery(sql);

        while ( rs.next() ) {
            camperId = rs.getInt("CAMPER_ID");
        }

        rs.close();
        stmt.close();
        c.close();

        return camperId;
        */



        return packetItems;
    }
}
