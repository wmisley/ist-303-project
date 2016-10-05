package com.cgu.ist303.project.dao.model;

/**
 * Created by will4769 on 9/29/16.
 */
public class Payment {
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private double amount = 0.0;
    private int camperId = 0;

    public int getCampSessionId() {
        return campSessionId;
    }

    public void setCampSessionId(int campSessionId) {
        this.campSessionId = campSessionId;
    }

    public int getCamperId() {
        return camperId;
    }

    public void setCamperId(int camperId) {
        this.camperId = camperId;
    }

    private int campSessionId = 0;

    public Payment() {
    }
}
