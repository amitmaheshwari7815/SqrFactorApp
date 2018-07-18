package com.hackerkernel.user.sqrfactor;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatFriends {
    public String userName,userProfile;
    public int userID;
    public JSONObject jsonObject;


    public ChatFriends() {

    }
    public ChatFriends(String userName, String userProfile, int userID) {
        this.userProfile = userProfile;
        this.userName = userName;
        this.userID = userID;

    }

    public ChatFriends(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        try {
            this.userID = jsonObject.getInt("id");
           this.userName = jsonObject.getString("first_name") +" "+jsonObject.getString("last_name");
           Log.v("name",this.userName);
           if(this.userName ==null) {
               this.userName = jsonObject.getString("name");
               Log.v("name2", this.userName);
           }
           this.userProfile = jsonObject.getString("profile");



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


        public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
