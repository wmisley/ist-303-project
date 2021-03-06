package com.cgu.ist303.project.dao.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CamperRegistration extends Camper {
    private double payment = 0.0;
    private int campSessionId = 0;
    private Date registrationDate = new Date();

    public String getRegistrationDateString() {
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        return dt1.format(registrationDate);
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isCheckedIn() {
        return isCheckedIn;
    }

    public String getCheckedInStatus() {
        if (isCheckedIn) {
            return "yes";
        } else {
            return "no";
        }
    }

    public void setCheckedIn(boolean checkedIn) {
        isCheckedIn = checkedIn;
    }

    private boolean isCheckedIn = false;

    public int getCampSessionId() {
        return campSessionId;
    }

    public void setCampSessionId(int campSessionId) {
        this.campSessionId = campSessionId;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

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
