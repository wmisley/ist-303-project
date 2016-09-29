package com.cgu.ist303.project.dao;

public class DAOFactor {
    static public String dbPath = "ist303.db";

    static public CamperDAO createCamperDAO() {
        return new SqliteCamperDAO(dbPath);
    }
    static public CampSessionDAO createCampSessionDAO() { return new SqliteCampSessionDAO(dbPath); }
    static public CamperRegistrationDAO createCamperRegistrationDAO() { return new SqliteCamperRegistrationDAO(dbPath); }
}
