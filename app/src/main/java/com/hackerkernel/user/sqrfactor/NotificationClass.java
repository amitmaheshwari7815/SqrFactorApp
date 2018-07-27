package com.hackerkernel.user.sqrfactor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class NotificationClass implements Serializable {

    private int notificationFromId;
    private String notificationSenderUrl;
    private int postId;
    private String notificationTitle;
    private String notificationBody;
    private String notificationType;
    public String type, time, name, profile, body, postType, title,description;
    public int userId, commentID, commentType;
    public JSONObject jsonObject;


    public NotificationClass(int userId, String profile, int postId, String postTitle, String shortDescription, String like) {

    }

    public NotificationClass(int notificationFromId, String notificationSenderUrl, int postId, String notificationTitle, String notificationBody, String notificationType, String type, String time, String name, String profile, String body, String postType, String title, String description, int userId, int commentID, int commentType, JSONObject jsonObject) {
        this.notificationFromId = notificationFromId;
        this.notificationSenderUrl = notificationSenderUrl;
        this.postId = postId;
        this.notificationTitle = notificationTitle;
        this.notificationBody = notificationBody;
        this.notificationType = notificationType;
        this.type = type;
        this.time = time;
        this.name = name;
        this.profile = profile;
        this.body = body;
        this.postType = postType;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.commentID = commentID;
        this.commentType = commentType;
        this.description = description;
        this.jsonObject = jsonObject;
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
            for (int j = 0; j < post.length(); j++) {
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

    public String getPostType() {
        return postType;
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

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
    public int getNotificationFromId() {
        return notificationFromId;
    }

    public void setNotificationFromId(int notificationFromId) {
        this.notificationFromId = notificationFromId;
    }

    public String getNotificationSenderUrl() {
        return notificationSenderUrl;
    }

    public void setNotificationSenderUrl(String notificationSenderUrl) {
        this.notificationSenderUrl = notificationSenderUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationBody() {
        return notificationBody;
    }

    public void setNotificationBody(String motificationBody) {
        this.notificationBody = motificationBody;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}
