package com.hackerkernel.user.sqrfactor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class NotificationClass implements Serializable {
    public String type, time, name, profile, body,postType,title,description;
    public int postId, userId, commentID, commentType;
    public JSONObject jsonObject;


    public NotificationClass(String type, String time, String name, String profile,String description, String body,String postType,String title, int userId, int postId, int commentID, int commentType) {
        this.type = type;
        this.time = time;
        this.name = name;
        this.profile = profile;
        this.body = body;
        this.userId = userId;
        this.postId = postId;
        this.commentID = commentID;
        this.commentType = commentType;
        this.postType = postType;
        this.title = title;
        this.description = description;
    }

    public NotificationClass(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        try {
            JSONObject type = jsonObject.getJSONObject("type");
            this.type = type.getString("type");

            JSONObject created = jsonObject.getJSONObject("created_at");
            this.time = created.getString("created_at");

            JSONObject userfrom = jsonObject.getJSONObject("user_from");
            this.name = userfrom.getString("name");
            this.profile = userfrom.getString("profile");

            JSONArray post = jsonObject.getJSONArray("post");
            for (int j = 0; j < post.length(); j++){
                JSONObject postObject = post.getJSONObject(j);
                this.postType = postObject.getString("type");
                this.title = postObject.getString("title");
                this.postId = postObject.getInt("id");
                this.userId = postObject.getInt("user_id");
                this.description = postObject.getString("description");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public String getPostType() {
        return postType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JSONObject getJsonObject() {
        return jsonObject;

    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}

