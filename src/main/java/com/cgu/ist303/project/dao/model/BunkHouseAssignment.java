package com.cgu.ist303.project.dao.model;

/**
 * Created by will4769 on 9/29/16.
 */
public class BunkHouseAssignment {
    public Camper getCamper() {
        return camper;
    }

    public void setCamper(Camper camper) {
        this.camper = camper;
    }

    private Camper camper = null;

    public BunkHouse getBunkHouse() {
        return bunkHouse;
    }

    public void setBunkHouse(BunkHouse bunkHouse) {
        this.bunkHouse = bunkHouse;
    }

    private BunkHouse bunkHouse = null;
}
