package com.cgu.ist303.project.dao.model;

/**
 * Created by will4769 on 9/29/16.
 */
public class TribeAssignment {
    private Tribe tribe = null;
    private Camper camper = null;

    public Camper getCamper() {
        return camper;
    }

    public void setCamper(Camper camper) {
        this.camper = camper;
    }

    public Tribe getTribe() {
        return tribe;
    }

    public void setTribe(Tribe tribe) {
        this.tribe = tribe;
    }
}
