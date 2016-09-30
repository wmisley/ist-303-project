package com.cgu.ist303.project.dao.model;

/**
 * Created by will4769 on 9/29/16.
 */
public class Tribe {
    public int tribeId = 0;

    public String getTribeName() {
        return tribeName;
    }

    public void setTribeName(String tribeName) {
        this.tribeName = tribeName;
    }

    public int getTribeId() {
        return tribeId;
    }

    public void setTribeId(int tribeId) {
        this.tribeId = tribeId;
    }

    public int getCampYear() {
        return campYear;
    }

    public void setCampYear(int campYear) {
        this.campYear = campYear;
    }

    public int campYear = 0;
    public String tribeName = "";

    public Tribe() {
    }
}
