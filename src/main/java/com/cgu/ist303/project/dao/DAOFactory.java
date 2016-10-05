package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.sqlite.SqliteCampSessionDAO;
import com.cgu.ist303.project.dao.sqlite.SqliteCamperDAO;
import com.cgu.ist303.project.dao.sqlite.SqliteCamperRegistrationDAO;
import com.cgu.ist303.project.dao.sqlite.SqliteTribeDAO;

public class DAOFactory {
    static public String dbPath = "ist303.db";

    static public CamperDAO createCamperDAO() {
        return new SqliteCamperDAO(dbPath);
    }
    static public CampSessionDAO createCampSessionDAO() { return new SqliteCampSessionDAO(dbPath); }
    static public CamperRegistrationDAO createCamperRegistrationDAO() { return new SqliteCamperRegistrationDAO(dbPath); }
    static public TribeDAO createTribeDAO() { return new SqliteTribeDAO(dbPath); }
}
