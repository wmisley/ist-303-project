package com.cgu.ist303.project.dao;

/**
 * Created by will4769 on 9/24/16.
 */
public class Camper {
    public enum Gender {
        Male,
        Female,
        Unspecified
    }

    private int camperId = 0;
    private String firstName = "";
    private String middleName = "";
    private String lastName = "";
    private int age = 0;
    private Gender gender = Gender.Unspecified;
    private String streetNumber = "";
    private String street = "";
    private String aptNumber = "";
    private String city = "";
    private String state = "";
    private String zipCode = "";
    private String phoneNumber = "";
    private String rpFirstName = "";
    private String rpMiddleName = "";
    private String rpLastName = "";

    public Camper() {
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAptNumber() {
        return aptNumber;
    }

    public void setAptNumber(String aptNumber) {
        this.aptNumber = aptNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRpFirstName() {
        return rpFirstName;
    }

    public void setRpFirstName(String rpFirstName) {
        this.rpFirstName = rpFirstName;
    }

    public String getRpMiddleName() {
        return rpMiddleName;
    }

    public void setRpMiddleName(String rpMiddleName) {
        this.rpMiddleName = rpMiddleName;
    }

    public String getRpLastName() {
        return rpLastName;
    }

    public void setRpLastName(String rpLastName) {
        this.rpLastName = rpLastName;
    }

    public int getCamperId() { return camperId; }

    public void setCamperId(int camperId) { this.camperId = camperId; }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String secondName) {
        this.lastName = secondName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


}
