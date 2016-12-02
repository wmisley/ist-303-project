package com.cgu.ist303.project.dao.model;

/**
 * Created by will4769 on 12/1/16.
 */
public class User {
    public enum UserType {
        Clerk(0),
        Director(1),
        Unspecified(2);

        private int value ;

        UserType(int value) {
            this.value = value ;
        }

        public int getValue() {
            return this.value;
        }
    }

    public String user = "";
    public String passwrod = "";

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswrod() {
        return passwrod;
    }

    public void setPasswrod(String passwrod) {
        this.passwrod = passwrod;
    }

    public UserType type = UserType.Unspecified;

    public User() {
    }
}
