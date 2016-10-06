package com.cgu.ist303.project.registrar;

import com.cgu.ist303.project.dao.CampSessionDAO;
import com.cgu.ist303.project.dao.CamperRegistrationDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.RejectedApplicationsDAO;
import com.cgu.ist303.project.dao.model.CampSession;

import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import com.cgu.ist303.project.dao.model.RejectedApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Registrar {
    private static final Logger log = LogManager.getLogger(Registrar.class);
    private List<CampSession> sessionList = new ArrayList<>();

    public Registrar() {

    }

    public void load(int year) throws Exception{
        log.debug("Querying camp sessions");

        CampSessionDAO sessionDAO = DAOFactory.createCampSessionDAO();
        sessionList = sessionDAO.query(year);
    }

    public List<CampSession> getSessions() {
        return sessionList;
    }

    public RejectedApplication.RejectionReason processApplication(Camper camper, CampSession session) throws  Exception {
        RejectedApplication.RejectionReason reason =
                RejectedApplication.RejectionReason.NotRejected;
        int campSessionId = session.getCampSessioId();
        int year = session.getCampYear();

        RejectedApplication ra = new RejectedApplication();
        ra.setCampSessionId(campSessionId);
        ra.setCamperId(camper.getCamperId());
        ra.setReason(reason);

        if ( isCamperAlreadyRegistered(camper, year) ) {
            log.info("Application rejected, camper already registered for the year");
            reason = RejectedApplication.RejectionReason.AlreadyRegisterForYear;

            rejecteApplication(ra);
        } else if ( isGenderLimitReached(campSessionId, camper.getGender() ) ) {
            log.info("Application rejected, gender limit reached");
            reason = RejectedApplication.RejectionReason.GenderLimitReached;

            rejecteApplication(ra);
        } else {
            CamperRegistration cr = new CamperRegistration();
            cr.setCampSessionId(campSessionId);
            cr.setCamperId(camper.getCamperId());

            registerCamper(cr);
        }

        return reason;
    }

    private boolean isCamperAlreadyRegistered(Camper camper, int year) throws Exception {
        CamperRegistrationDAO dao = DAOFactory.createCamperRegistrationDAO();
        return dao.queryIsCamperRegisterdForYear(camper, year);
    }

    private boolean isGenderLimitReached(int campSessionId, Camper.Gender gender) throws Exception {
        Map<Integer, CampSession> sessionMap =
                sessionList.stream().collect(
                        Collectors.toMap(CampSession::getCampSessioId, item -> item));

        CampSession session = sessionMap.get(campSessionId);
        CamperRegistrationDAO dao = DAOFactory.createCamperRegistrationDAO();
        int genderCount = dao.queryGenderCount(session.getCampSessioId(), gender);

        return (genderCount >= session.getGenderLimit());
    }

    private void registerCamper(CamperRegistration cr) throws Exception {
        CamperRegistrationDAO dao = DAOFactory.createCamperRegistrationDAO();
        dao.insert(cr);
    }

    private void rejecteApplication(RejectedApplication ra) throws Exception {
        RejectedApplicationsDAO dao = DAOFactory.createRejectedApplicationsDAO();
        dao.insert(ra);
    }
}
