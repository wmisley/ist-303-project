package com.cgu.ist303.project.registrar;

import com.cgu.ist303.project.dao.*;
import com.cgu.ist303.project.dao.model.*;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TribeAssigner {
    //private int tribeIndex = 0;

    public TribeAssigner() {
    }

    public void assign(int year, int campSessionId) throws Exception {
        //tribeIndex = 0;

        //Get registered campers for the session
        CamperRegistrationDAO dao = DAOFactory.createCamperRegistrationDAO();
        ObservableList<CamperRegistration> regCampers = dao.queryRegisteredCampers(year, campSessionId, true);

        //Get the tribes for the session
        TribeDAO tribeDAO = DAOFactory.createTribeDAO();
        ObservableList<Tribe> tribes = tribeDAO.query(campSessionId);


        List<CamperRegistration> boys = regCampers.stream()
                .filter(c -> c.getGender() == Camper.Gender.Male)
                .collect(Collectors.toList());

        List<CamperRegistration> girls = regCampers.stream()
                .filter(c -> c.getGender() == Camper.Gender.Female)
                .collect(Collectors.toList());


        List<TribeAssignmentById> boyAssignments = assign(boys, tribes);
        List<TribeAssignmentById> girlAssignments = assign(girls, tribes);

        //Delete previous assignments and save the new assignments
        TribeAssignmentDAO taDAO = DAOFactory.createTribeAssignmentDAO();
        taDAO.delete(tribes);
        taDAO.insert(boyAssignments);
        taDAO.insert(girlAssignments);
    }

    private List<TribeAssignmentById>  assign(List<CamperRegistration> regCampers, ObservableList<Tribe> tribes) {
        int tribeIndex = 0;
        List<TribeAssignmentById> tas = new ArrayList<>();
        int tribeCount = tribes.size();

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

        return tas;
    }

    public void clearAssignments(int campSessionId) throws Exception {
        TribeDAO bhDAO = DAOFactory.createTribeDAO();
        List<Tribe> tribes = bhDAO.query(campSessionId);

        TribeAssignmentDAO taDAO = DAOFactory.createTribeAssignmentDAO();
        taDAO.delete(tribes);
    }
}
