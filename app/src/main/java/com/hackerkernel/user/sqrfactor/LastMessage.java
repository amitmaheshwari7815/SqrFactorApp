package com.hackerkernel.user.sqrfactor;

public class LastMessage {

    private int senderId;
    private String message;

    public LastMessage()
    {

    }


    public LastMessage(int senderId, String message) {
        this.senderId = senderId;
        this.message = message;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
