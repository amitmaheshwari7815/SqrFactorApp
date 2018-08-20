package com.hackerkernel.user.sqrfactor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class UserClass implements Serializable{

    private JSONObject jsonObject;
    private String user_name;
    private String first_name;
    private String last_name;
    private String profile;
    private int userId;
    private String email;
    private String mobile;
    private String userType;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }



    public UserClass(String user_name, String first_name, String last_name, String profile, int userId, String email, String mobile, String userType) {
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile = profile;
        this.userId = userId;
        this.email = email;
        this.mobile = mobile;
        this.userType = userType;
    }



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

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public UserClass(String user_name, String first_name, String last_name, String profile) {
        this.user_name = user_name;
        this.first_name = first_name;

        this.last_name = last_name;
        this.profile = profile;
    }

    public UserClass(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        try {
            JSONObject user = jsonObject.getJSONObject("user");
            this.userId = user.getInt("id");
            this.user_name = user.getString("user_name");
            this.first_name=user.getString("first_name");
            this.last_name=user.getString("last_name");
            this.profile=user.getString("profile");
            this.email=user.getString("email");
            this.mobile=user.getString("mobile_number");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
