package com.cgu.ist303.project.registrar;

import com.cgu.ist303.project.dao.*;
import com.cgu.ist303.project.dao.model.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
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

    public int insertCamperRecord(Camper camper) throws Exception {
        log.debug("Inserting new camper record");

        CamperDAO camperDAO = DAOFactory.createCamperDAO();

        int camperId = camperDAO.queryCamperId(camper);

        if (camperId == Camper.CAMPER_ID_NOT_IN_SYSTEM) {
            camperId = camperDAO.insertCamper(camper);
        }

        return camperId;
    }

    public void rejectIncompleteApplication(Camper camper, CampSession session) throws Exception {
        log.info("Application rejected, form not complete");

        RejectedApplication ra = new RejectedApplication();
        ra.setCampSessionId(session.getCampSessioId());
        ra.setCamperId(camper.getCamperId());
        ra.setReason(RejectedApplication.RejectionReason.ApplicationIncomplete);

        rejecteApplication(ra);
    }

    public RejectedApplication.RejectionReason processApplication(Camper camper, CampSession session, double payment)
            throws  Exception {
        RejectedApplication.RejectionReason reason =
                RejectedApplication.RejectionReason.NotRejected;
        int campSessionId = session.getCampSessioId();
        int year = session.getCampYear();

        RejectedApplication ra = new RejectedApplication();
        ra.setCampSessionId(campSessionId);
        ra.setCamperId(camper.getCamperId());


        if ( !wasReceivedInAllowableTimeframe(session) ) {
            log.info("Application rejected, not received within allowable time frame");
            reason = RejectedApplication.RejectionReason.NotReceivedDuringAllowableTimeframe;
            ra.setReason(reason);

            rejecteApplication(ra);
        } if ( isCamperAlreadyRegistered(camper, year) ) {
            log.info("Application rejected, camper already registered for the year");
            reason = RejectedApplication.RejectionReason.AlreadyRegisterForYear;
            ra.setReason(reason);

            rejecteApplication(ra);
        } else if ( isGenderLimitReached(campSessionId, camper.getGender() ) ) {
            log.info("Application rejected, gender limit reached");
            reason = RejectedApplication.RejectionReason.GenderLimitReached;
            ra.setReason(reason);

            rejecteApplication(ra);
        } else if ( !isApplicantAppropriateAge(camper.getAge()) ) {
            log.info("Application rejected, not appropriate age");
            reason = RejectedApplication.RejectionReason.CamperNotInAgeRange;
            ra.setReason(reason);

            rejecteApplication(ra);
        } else {
            CamperRegistrationRecord cr = new CamperRegistrationRecord();
            cr.setCampSessionId(campSessionId);
            cr.setCamperId(camper.getCamperId());

            registerCamper(cr);
            insertPayment(camper.getCamperId(), session.getCampSessioId(), payment);
        }

        return reason;
    }

    private boolean isApplicantAppropriateAge(int age) {
        return ((age >= 9) && (age <= 18));
    }

    private void insertPayment(int camperId, int sessionId, double payValue) throws Exception {
        PaymentDAO dao = DAOFactory.createPaymentDAO();
        Payment payment = new Payment();
        payment.setCamperId(camperId);
        payment.setCampSessionId(sessionId);
        payment.setAmount(payValue);


        log.debug("Inserting payment record");
        dao.insert(payment);
    }

    private boolean wasReceivedInAllowableTimeframe(CampSession session) {
        Calendar calCampStart = Calendar.getInstance();
        calCampStart.set(Calendar.DAY_OF_MONTH, session.getStartDay());
        calCampStart.set(Calendar.MONTH, session.getStartMonth() - 1);
        calCampStart.set(Calendar.YEAR, session.getCampYear());
        log.debug("calCampStart:{}", calCampStart.getTime());

        Calendar calEarliest = (Calendar) calCampStart.clone();
        calEarliest.add(Calendar.MONTH, -8);
        Date dateEarliest = calEarliest.getTime();
        log.debug("calEarliest:{}", dateEarliest);

        Calendar calLatest = (Calendar) calCampStart.clone();
        calLatest.add(Calendar.MONTH, -2);
        Date dateLatest = calLatest.getTime();
        log.debug("dateLatest:{}", dateLatest);

        Date dateToday = new Date();
        Date dateAppReceived = dateToday;
        boolean isAllowable = false;

        if (dateAppReceived.before(dateEarliest)) {
            log.debug("Application received to early, can't receive before {}", dateEarliest);
        } else if (dateAppReceived.after(dateLatest)) {
            log.debug("Application received to late, can't receive after {}", dateLatest);
        } else {
            isAllowable = true;
        }

        return isAllowable;
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

    private void registerCamper(CamperRegistrationRecord cr) throws Exception {
        CamperRegistrationDAO dao = DAOFactory.createCamperRegistrationDAO();
        dao.insert(cr);
    }

    private void rejecteApplication(RejectedApplication ra) throws Exception {
        RejectedApplicationsDAO dao = DAOFactory.createRejectedApplicationsDAO();
        dao.insert(ra);
    }
}
