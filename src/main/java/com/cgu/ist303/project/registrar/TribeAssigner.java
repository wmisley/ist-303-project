package com.cgu.ist303.project.registrar;

import com.cgu.ist303.project.dao.*;
import com.cgu.ist303.project.dao.model.*;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by will4769 on 11/2/16.
 */
public class TribeAssigner {
    public TribeAssigner() {
    }

    public void assign(int year, int campSessionId) throws Exception {
        List<TribeAssignmentById> tas = new ArrayList<>();
        CamperRegistrationDAO dao = DAOFactory.createCamperRegistrationDAO();
        ObservableList<CamperRegistration> regCampers = dao.queryRegisteredCampers(year, campSessionId, true);

        TribeDAO tribeDAO = DAOFactory.createTribeDAO();
        ObservableList<Tribe> tribes = tribeDAO.query(campSessionId);
        int tribeCount = tribes.size();
        int tribeIndex = 0;

        for (CamperRegistration cr : regCampers) {
            TribeAssignmentById ta = new TribeAssignmentById();

            ta.setCamperId(cr.getCamperId());
            ta.setTribe(tribes.get(tribeIndex));

            tas.add(ta);

            if (tribeIndex == (tribeCount -1)) {
                tribeIndex = 0;
            } else {
                tribeIndex++;
            }
        }

        TribeAssignmentDAO taDAO = DAOFactory.createTribeAssignmentDAO();
        taDAO.delete(tribes);
        taDAO.insert(tas);
    }

    public void clearAssignments(int campSessionId) throws Exception {
        TribeDAO bhDAO = DAOFactory.createTribeDAO();
        List<Tribe> tribes = bhDAO.query(campSessionId);

        TribeAssignmentDAO taDAO = DAOFactory.createTribeAssignmentDAO();
        taDAO.delete(tribes);
    }
}
