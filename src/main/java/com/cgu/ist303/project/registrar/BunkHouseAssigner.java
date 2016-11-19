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
        List<BunkHouseAssignmentById> bas = new ArrayList<>();
        CamperRegistrationDAO dao = DAOFactory.createCamperRegistrationDAO();
        ObservableList<CamperRegistration> regCampers = dao.queryRegisteredCampers(year, campSessionId, true);

        BunkHouseDAO bhDAO = DAOFactory.createBunkHouseDAO();
        ObservableList<BunkHouse> bunkHouses = bhDAO.query(campSessionId);
        int hbCount = bunkHouses.size();
        int bhIndex = 0;

        for (CamperRegistration cr : regCampers) {
            BunkHouseAssignmentById bha = new BunkHouseAssignmentById();

            bha.setCamperId(cr.getCamperId());
            bha.setBunkHouse(bunkHouses.get(bhIndex));

            bas.add(bha);

            if (bhIndex == (hbCount - 1)) {
                bhIndex = 0;
            } else {
                bhIndex++;
            }
        }

        BunkHouseAssignmentDAO taDAO = DAOFactory.createBunkHouseAssignmentDAO();
        taDAO.delete(bunkHouses);
        taDAO.insert(bas);
    }
}
