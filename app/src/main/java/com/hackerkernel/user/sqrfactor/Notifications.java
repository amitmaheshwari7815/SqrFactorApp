package com.hackerkernel.user.sqrfactor;

import org.json.JSONObject;

import java.io.Serializable;

public class Notifications implements Serializable {
    public String type,time,name,profile;
    public int postId, userId,commentID, commentType,body;
    public JSONObject jsonObject;

    {
    }
}
