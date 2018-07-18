package com.hackerkernel.user.sqrfactor;

import java.io.Serializable;

public class UserClass implements Serializable{

    public String user_name,first_name,last_name,profile;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public UserClass(String user_name, String first_name, String last_name, String profile) {
        this.user_name = user_name;
        this.first_name = first_name;

        this.last_name = last_name;
        this.profile = profile;
    }
}
