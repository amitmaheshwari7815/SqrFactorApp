package com.hackerkernel.user.sqrfactor;

import java.io.Serializable;

public class PushNotificationClass implements Serializable {
    private int notificationFromId;
    private String notificationSenderUrl;
    private int postId;
    private String notificationTitle;
    private String notificationBody;
    private String notificationType;
    private String notificationSenderName;

    public PushNotificationClass()
    {

    }
    public PushNotificationClass(int notificationFromId, String notificationSenderUrl, int postId, String notificationTitle, String notificationBody, String notificationType,String notificationSenderName) {
        this.notificationFromId = notificationFromId;
        this.notificationSenderUrl = notificationSenderUrl;
        this.postId = postId;
        this.notificationTitle = notificationTitle;
        this.notificationBody = notificationBody;
        this.notificationType = notificationType;
        this.notificationSenderName = notificationSenderName;
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

    public String getNotificationSenderName() {
        return notificationSenderName;
    }

    public void setNotificationSenderName(String notificationSenderName) {
        this.notificationSenderName = notificationSenderName;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}



