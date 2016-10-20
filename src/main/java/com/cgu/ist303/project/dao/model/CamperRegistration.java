package com.cgu.ist303.project.dao.model;

/**
 * Created by will4769 on 10/19/16.
 */
public class CamperRegistration extends Camper {
    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    private double payment = 0.0;

    public double getAmountDue() {
        return 1000.0 - getPayment();
    }

    public String getFormattedAmountDue() {
        return String.format("$%,.2f", getAmountDue());
    }

    public String getMiddleInitial() {
        if (getMiddleName().length() >=  1) {
            return getMiddleName().substring(0, 1);
        } else {
            return "-";
        }
    }
}
