package com.cgu.ist303.project.dao.model;

/**
 * Created by will4769 on 9/29/16.
 */
public class CamperRegistration {
    private int campSessionId = 0;

    public int getCamperId() {
        return camperId;
    }

    public void setCamperId(int camperId) {
        this.camperId = camperId;
    }

    public int getCampSessionId() {
        return campSessionId;
    }

    public void setCampSessionId(int campSessionId) {
        this.campSessionId = campSessionId;
    }

    private int camperId = 0;

    public CamperRegistration() {
    }
}
