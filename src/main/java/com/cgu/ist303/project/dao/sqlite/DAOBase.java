package com.cgu.ist303.project.dao.sqlite;

/**
 * Created by will4769 on 10/20/16.
 */
public class DAOBase {
    protected String dbFilepath = "";

    public DAOBase(String sqliteFilepath) {
        dbFilepath = sqliteFilepath;
    }
}
