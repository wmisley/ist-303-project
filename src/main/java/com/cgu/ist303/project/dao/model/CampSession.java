package com.cgu.ist303.project.dao.model;


public class CampSession {
    private int campSessioId = 0;
    private int campYear = 0;
    private int startMonth = 0;
    private int startDay = 0;
    private int endMonth = 0;
    private int endDay = 0;
    private int genderLimit = 0;

    public CampSession() {
    }

    public int getCampSessioId() {
        return campSessioId;
    }

    public void setCampSessioId(int campSessioId) {
        this.campSessioId = campSessioId;
    }

    public int getCampYear() {
        return campYear;
    }

    public void setCampYear(int campYear) {
        this.campYear = campYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int getGenderLimit() {
        return genderLimit;
    }

    public void setGenderLimit(int genderLimit) {
        this.genderLimit = genderLimit;
    }
}
