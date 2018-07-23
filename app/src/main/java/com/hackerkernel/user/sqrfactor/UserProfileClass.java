package com.hackerkernel.user.sqrfactor;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserProfileClass {
    private String user_name,name,first_name,last_name,profile,email,mobile_number,user_type,postType,post_title,banner_image,short_description,description;
    private String like,comment,share;
    private int user_id,post_id,post_user_id;
    private JSONObject jsonObject;

    public UserProfileClass(int user_id,String name, String user_name, String first_name, String last_name, String profile, String email, String mobile_number, String user_type) {
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile = profile;
        this.email = email;
        this.mobile_number = mobile_number;
        this.user_type = user_type;
        this.user_id = user_id;
        this.name = name;
    }
    public UserProfileClass(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        try {
            JSONObject user = jsonObject.getJSONObject("user");
            this.user_id = user.getInt("id");
            this.user_name = user.getString("user_name");
            this.first_name = user.getString("first_name");
            this.last_name = user.getString("last_name");
            this.profile = user.getString("profile");
            this.email = user.getString("email");
            this.mobile_number = user.getString("mobile_number");
            this.name = user.getString("name");

            JSONObject jsonPost = jsonObject.getJSONObject("posts");
            JSONArray jsonArrayData = jsonPost.getJSONArray("data");
            for (int i = 0; i < jsonArrayData.length(); i++) {
                JSONObject jsonObject1= jsonArrayData.getJSONObject(i);


            }

        } catch(JSONException e){
                e.printStackTrace();
            }
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

    }
}
