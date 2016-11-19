package com.cgu.ist303.project.dao.model;


public class BunkHouseAssignmentById {
    public int getCamperId() {
        return camperId;
    }

    public void setCamperId(int camperId) {
        this.camperId = camperId;
    }

    public int camperId = 0;

    public BunkHouse getBunkHouse() {
        return bunkHouse;
    }

    public void setBunkHouse(BunkHouse bunkHouse) {
        this.bunkHouse = bunkHouse;
    }

    public BunkHouse bunkHouse = null;

    public BunkHouseAssignmentById() {
    }
}
