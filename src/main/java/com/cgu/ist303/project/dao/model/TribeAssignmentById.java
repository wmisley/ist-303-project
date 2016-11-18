package com.cgu.ist303.project.dao.model;

/**
 * Created by will4769 on 11/17/16.
 */
public class TribeAssignmentById {
    private Tribe tribe = null;

    public int getCamperId() {
        return camperId;
    }

    public void setCamperId(int camperId) {
        this.camperId = camperId;
    }

    private int camperId = 0;

    public Tribe getTribe() {
        return tribe;
    }

    public void setTribe(Tribe tribe) {
        this.tribe = tribe;
    }
}
