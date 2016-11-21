package com.cgu.ist303.project.registrar;


import com.cgu.ist303.project.dao.*;
import com.cgu.ist303.project.dao.model.*;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class BunkHouseAssigner {
    public BunkHouseAssigner() {
    }

    public void assign(int year, int campSessionId) throws Exception {
        GenderBunkHouseAssigner maleAssigner = new GenderBunkHouseAssigner(campSessionId, BunkHouse.Gender.Male);
        GenderBunkHouseAssigner femaleAssigner = new GenderBunkHouseAssigner(campSessionId, BunkHouse.Gender.Female);

        CamperRegistrationDAO dao = DAOFactory.createCamperRegistrationDAO();
        ObservableList<CamperRegistration> regCampers = dao.queryRegisteredCampers(year, campSessionId, true);

        for (CamperRegistration cr : regCampers) {
            if (cr.getGender() == Camper.Gender.Male) {
                maleAssigner.assign(cr.getCamperId());
            } else {
                femaleAssigner.assign(cr.getCamperId());
            }
        }

        BunkHouseDAO bhDAO = DAOFactory.createBunkHouseDAO();
        ObservableList<BunkHouse> bunkHouses = bhDAO.query(campSessionId);
        BunkHouseAssignmentDAO taDAO = DAOFactory.createBunkHouseAssignmentDAO();
        taDAO.delete(bunkHouses);

        List<BunkHouseAssignmentById> bas = new ArrayList<>();
        bas.addAll(maleAssigner.getAssignments());
        bas.addAll(femaleAssigner.getAssignments());
        taDAO.insert(bas);
    }
}
