package com.hackerkernel.user.sqrfactor;

import java.io.Serializable;

public class NotificationClass implements Serializable {

    private int notificationFromId;
    private String notificationSenderUrl;
    private int postId;
    private String notificationTitle;
    private String notificationBody;
    private String notificationType;

    public NotificationClass()
    {

    }
    public NotificationClass(int notificationFromId, String notificationSenderUrl, int postId, String notificationTitle, String notificationBody, String notificationType) {
        this.notificationFromId = notificationFromId;
        this.notificationSenderUrl = notificationSenderUrl;
        this.postId = postId;
        this.notificationTitle = notificationTitle;
        this.notificationBody = notificationBody;
        this.notificationType = notificationType;
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

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}
