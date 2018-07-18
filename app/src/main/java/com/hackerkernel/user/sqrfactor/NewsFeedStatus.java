package com.hackerkernel.user.sqrfactor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsFeedStatus implements Serializable {
    public String userImageUrl,authImageUrl,authName,time,postTitle,shortDescription,postImage,fullDescription;
    public String like,comments,share;
    public String commentProfileImageUrl,commentUserName,commentTime,commentDescription,commentLike;
    public int postId,userId;
    public transient JSONObject jsonObject;
    public ArrayList<comments_limited> commentsLimitedArrayList=new ArrayList<>();




    public NewsFeedStatus(int postId, int userId,String userImageUrl, String authImageUrl, String authName, String time, String postTitle, String shortDescription, String postImage, String like, String comments, String share, String commentProfileImageUrl, String commentUserName, String commentTime, String commentDescription, String commentLike) {
        this.userImageUrl = userImageUrl;
        this.authImageUrl = authImageUrl;
        this.authName = authName;
        this.time = time;
        this.postTitle = postTitle;
        this.shortDescription = shortDescription;
        this.postImage = postImage;
        this.like = like;
        this.comments = comments;
        this.share = share;
        this.commentProfileImageUrl = commentProfileImageUrl;
        this.commentUserName = commentUserName;
        this.commentTime = commentTime;
        this.commentDescription = commentDescription;
        this.commentLike = commentLike;
        this.postId = postId;
        this.userId = userId;
    }


    public ArrayList<comments_limited> getCommentsLimitedArrayList() {
        return commentsLimitedArrayList;
    }

    public void setCommentsLimitedArrayList(ArrayList<comments_limited> commentsLimitedArrayList) {
        this.commentsLimitedArrayList = commentsLimitedArrayList;
    }

    public NewsFeedStatus(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        try {
                this.postId = jsonObject.getInt("id");
                this.postTitle = jsonObject.getString("title");
                this.userId = jsonObject.getInt("user_id");
                this.userImageUrl = jsonObject.getString("image");
                this.postImage = jsonObject.getString("banner_image");
                this.shortDescription = jsonObject.getString("short_description");
                this.time =jsonObject.getString("updated_at");
                this.comments =jsonObject.getString("comments_count");

                JSONObject user = jsonObject.getJSONObject("user");
                this.authName=user.getString("first_name")+ user.getString("last_name");
                this.authImageUrl = user.getString("profile");

                JSONArray likes = jsonObject.getJSONArray("likes");
                this.like = likes.length()+"";

               JSONArray commentsLimited=jsonObject.getJSONArray("comments_limited");

               for(int i=0;i<commentsLimited.length();i++)
               {
                   try {
                       JSONObject jsonObject1=commentsLimited.getJSONObject(i);

                       comments_limited limited=new comments_limited(jsonObject1.getJSONArray("likes").length(),jsonObject1.getJSONObject("user").getString("first_name")+" "+jsonObject1.getJSONObject("user").getString("last_name"),
                               jsonObject1.getJSONObject("user").getString("profile"),jsonObject1.getInt("id"),jsonObject1.getInt("user_id"),
                               jsonObject1.getInt("commentable_id"),jsonObject1.getString("commentable_type"),
                               jsonObject1.getString("body"),jsonObject1.getString("created_at"),jsonObject1.getString("updated_at"));
                      this.commentsLimitedArrayList.add(limited);


                   } catch (JSONException e) {
                       e.printStackTrace();
                   }


               }




//            this.comments = jsonObject.getString("comments_count");
//            this.share = jsonObject.getString("is_shared");
//            this.like = jsonObject.getString("likes");



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

            public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getAuthImageUrl() {
        return authImageUrl;
    }

    public void setAuthImageUrl(String authImageUrl) {
        this.authImageUrl = authImageUrl;
    }


    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getCommentProfileImageUrl() {
        return commentProfileImageUrl;
    }

    public void setCommentProfileImageUrl(String commentProfileImageUrl) {
        this.commentProfileImageUrl = commentProfileImageUrl;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentDescription() {
        return commentDescription;
    }

    public void setCommentDescription(String commentDescription) {
        this.commentDescription = commentDescription;
    }

    public String getCommentLike() {
        return commentLike;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
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

    public void setCommentLike(String commentLike) {
        this.commentLike = commentLike;
    }
}