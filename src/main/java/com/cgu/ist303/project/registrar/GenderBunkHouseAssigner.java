package com.cgu.ist303.project.registrar;

import com.cgu.ist303.project.dao.BunkHouseDAO;
import com.cgu.ist303.project.dao.DAOFactory;
import com.cgu.ist303.project.dao.model.BunkHouse;
import com.cgu.ist303.project.dao.model.BunkHouseAssignmentById;

import java.util.ArrayList;
import java.util.List;

/**
 * Assigns campers to a gender specific set of bunk houses
 */
public class GenderBunkHouseAssigner {
    private BunkHouse.Gender gender = BunkHouse.Gender.Unspecified;
    private int campSessionId = 0;
    private int bhIndex = 0;

    private List<BunkHouseAssignmentById> assignments = new ArrayList<>();
    private List<BunkHouse> bunkHouses = null;

    public GenderBunkHouseAssigner(int csId, BunkHouse.Gender g) {
        gender = g;
        campSessionId = csId;
    }

    /**
     * The assumption entering this method is that campers are feed in
     * in sorted order of age.
     * @param camperId
     * @throws Exception
     */
    public void assign(int camperId) throws Exception {
        if (bunkHouses == null) {
            BunkHouseDAO dao = DAOFactory.createBunkHouseDAO();
            bunkHouses = dao.query(campSessionId, gender);
        }

        BunkHouseAssignmentById bha = new BunkHouseAssignmentById();
        bha.setBunkHouse(bunkHouses.get(bhIndex));
        bha.setCamperId(camperId);

        assignments.add(bha);

        if (bhIndex == (bunkHouses.size() - 1)) {
            bhIndex = 0;
        } else {
            bhIndex++;
        }
    }

    public List<BunkHouseAssignmentById> getAssignments() {
        return this.assignments;
    }
}
