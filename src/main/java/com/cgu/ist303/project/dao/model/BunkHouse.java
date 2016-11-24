package com.cgu.ist303.project.dao.model;

/**
 * Created by will4769 on 9/29/16.
 */
public class BunkHouse {
    public enum Gender {
        Male(0),
        Female(1),
        Unspecified(2);

        private int value ;

        Gender(int value) {
            this.value = value ;
        }

        public int getValue() {
            return this.value;
        }
    }

    private String bunkHouseName = "";
    private int campSessionId = 0;
    private Gender gender = Gender.Unspecified;
    private int maxOccupants = 0;

    private int bunkHouseId = 0;

    public int getMaxOccupants() {
        return maxOccupants;
    }

    public void setMaxOccupants(int maxOccupants) {
        this.maxOccupants = maxOccupants;
    }

    public int getBunkHouseId() {
        return bunkHouseId;
    }

    public void setBunkHouseId(int bunkHouseId) {
        this.bunkHouseId = bunkHouseId;
    }

    public String getBunkHouseName() {
        return bunkHouseName;
    }

    public void setBunkHouseName(String bunkHouseName) {
        this.bunkHouseName = bunkHouseName;
    }

    public int getCampSessionId() {
        return campSessionId;
    }

    public void setCampSessionId(int campSessionId) {
        this.campSessionId = campSessionId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String toString() {
        return bunkHouseName;
    }
}
