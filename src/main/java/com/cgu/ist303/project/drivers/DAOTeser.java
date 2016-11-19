package com.cgu.ist303.project.drivers;

import com.cgu.ist303.project.dao.*;
import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.CamperRegistrationRecord;
import com.cgu.ist303.project.dao.model.TribeAssignment;
import com.cgu.ist303.project.dao.sqlite.SqliteDBCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by will4769 on 9/29/16.
 */
public class DAOTeser {
    private static final Logger log = LogManager.getLogger(DAOTeser.class);

    static public void main2(String[] args) {
        try {
            Runtime.getRuntime().exec(new String[]{"open", "-a", "Microsoft Word" , "test.txt"});
            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.print(e.getMessage());
            e.printStackTrace();
        }
    }

    static public void main(String[] args) {
        try {
            DAOFactory.dbPath = "ist303-term2- bak.db";
            TribeAssignmentDAO taDAO = DAOFactory.createTribeAssignmentDAO();
            List<TribeAssignment> list = taDAO.query(2);

            for (TribeAssignment ta : list) {
                log.info("ID:{}, Name:{}", ta.getTribe().getTribeId(), ta.getTribe().getTribeId());
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
            e.printStackTrace();
        }
    }

    public void registerCamper(String dbPath, ArrayList<CampSession> sessions, int camperId) {
        DAOFactory.dbPath = dbPath;
        CamperRegistrationDAO regDAO = DAOFactory.createCamperRegistrationDAO();
        CamperRegistrationRecord reg = new CamperRegistrationRecord();
        reg.setCamperId(camperId);
        reg.setCampSessionId(sessions.get(0).getCampSessioId());

        try {
            regDAO.insert(reg);
        } catch (Exception e) { log.error(e); }
    }

    public int saveCamper(String dbPath) {
        DAOFactory.dbPath = dbPath;
        CamperDAO camperDAO = DAOFactory.createCamperDAO();

        Camper camper = new Camper();
        camper.setFirstName("Joseph");
        camper.setMiddleName("Michael");
        camper.setLastName("Isley");
        camper.setAge(9);
        camper.setGender(Camper.Gender.Male);
        camper.setAptNumber("");
        camper.setStreetNumber("380");
        camper.setStreet("New York St.");
        camper.setCity("Redlands");
        camper.setState("CA");
        camper.setZipCode("91791");
        camper.setRpFirstName("Bobby");
        camper.setFirstName("");
        camper.setRpLastName("Boon");

        try {
            log.debug("Inserting a camper record");
            int camperId = camperDAO.insertCamper(camper);
            camper.setCamperId(camperId);
        } catch (Exception e) {
            log.error(e);
        }

        return camper.getCamperId();
    }
}
