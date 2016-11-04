package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.sqlite.*;

public class DAOFactory {
    static public String dbPath = "ist303.db";

    static public CamperDAO createCamperDAO() {
        return new SqliteCamperDAO(dbPath);
    }
    static public CampSessionDAO createCampSessionDAO() { return new SqliteCampSessionDAO(dbPath); }
    static public CamperRegistrationDAO createCamperRegistrationDAO() { return new SqliteCamperRegistrationDAO(dbPath); }
    static public TribeDAO createTribeDAO() { return new SqliteTribeDAO(dbPath); }
    static public PaymentDAO createPaymentDAO() { return new SqlitePaymentDAO(dbPath); }
    static public RejectedApplicationsDAO createRejectedApplicationsDAO() { return new SqliteRejectedApplicationDAO(dbPath); }
    static public EmergencyContactDAO createEmergencyContactDAO() { return new SqliteEmergencyContactDAO(dbPath); }
    static public BunkHouseDAO createBunkHouseDAO() { return new SqliteBunkHouseDAO(dbPath); }
    static public TribeAssignmentDAO createTribeAssignmentDAO() { return new SqliteTribeAssignmentDAO(dbPath); }
}
