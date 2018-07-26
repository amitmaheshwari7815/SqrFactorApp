package com.hackerkernel.user.sqrfactor;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsFeedStatus implements Serializable {
    private String type,credits_earned,is_Shared,deleted_at,paid_at,week_views,user_name_of_post;
    private int user_post_id;
    public String userImageUrl,authImageUrl,authName,time,postTitle,shortDescription,postImage,fullDescription;
    public String like,comments,share,slug;
    public String commentProfileImageUrl,commentUserName,commentTime,commentDescription,commentLike;
    public int postId,userId,sharedId, commentId;
    public transient JSONObject jsonObject;
    public ArrayList<comments_limited> commentsLimitedArrayList=new ArrayList<>();


    public NewsFeedStatus(String type, String credits_earned, String is_Shared, String deleted_at, String paid_at, String week_views, String user_name_of_post, int user_post_id, String userImageUrl, String authImageUrl, String authName, String time, String postTitle, String shortDescription, String postImage, String fullDescription, String like, String comments, String share, String slug, String commentProfileImageUrl, String commentUserName, String commentTime, String commentDescription, String commentLike, int postId, int userId, int sharedId, int commentId, JSONObject jsonObject, ArrayList<comments_limited> commentsLimitedArrayList) {
        this.type = type;
        this.credits_earned = credits_earned;
        this.is_Shared = is_Shared;
        this.deleted_at = deleted_at;
        this.paid_at = paid_at;
        this.week_views = week_views;
        this.user_name_of_post = user_name_of_post;
        this.user_post_id = user_post_id;
        this.userImageUrl = userImageUrl;
        this.authImageUrl = authImageUrl;
        this.authName = authName;
        this.time = time;
        this.postTitle = postTitle;
        this.shortDescription = shortDescription;
        this.postImage = postImage;
        this.fullDescription = fullDescription;
        this.like = like;
        this.comments = comments;
        this.share = share;
        this.slug = slug;
        this.commentProfileImageUrl = commentProfileImageUrl;
        this.commentUserName = commentUserName;
        this.commentTime = commentTime;
        this.commentDescription = commentDescription;
        this.commentLike = commentLike;
        this.postId = postId;
        this.userId = userId;
        this.sharedId = sharedId;
        this.commentId = commentId;
        this.jsonObject = jsonObject;
        this.commentsLimitedArrayList = commentsLimitedArrayList;
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
                this.slug=jsonObject.getString("slug");
                this.type=jsonObject.getString("type");
                this.postTitle = jsonObject.getString("title");
                this.userId = jsonObject.getInt("user_id");
                this.userImageUrl = jsonObject.getString("image");
                this.postImage = jsonObject.getString("banner_image");
                this.shortDescription = jsonObject.getString("short_description");
                this.fullDescription = jsonObject.getString("description");
                this.credits_earned=jsonObject.getString("credits_earned");
                this.week_views=jsonObject.getString("week_views");
                this.credits_earned=jsonObject.getString("credits_earned");
                this.paid_at=jsonObject.getString("paid_at");
                this.deleted_at=jsonObject.getString("deleted_at");
                this.user_post_id=jsonObject.getInt("user_post_id");
                this.is_Shared=jsonObject.getString("is_shared");
                this.time =jsonObject.getString("updated_at");
                this.comments =jsonObject.getString("comments_count");
                this.sharedId = jsonObject.getInt("shared_id");

                JSONObject user = jsonObject.getJSONObject("user");
                this.authName=user.getString("first_name")+ user.getString("last_name");
                this.authImageUrl = user.getString("profile");
                this.user_name_of_post=user.getString("user_name");

                JSONArray likes = jsonObject.getJSONArray("likes");
                this.like = likes.length()+"";

               JSONArray commentsLimited=jsonObject.getJSONArray("comments_limited");

               for(int i=0;i<commentsLimited.length();i++)
               {
                   try {
                       JSONObject jsonObject1=commentsLimited.getJSONObject(i);
                        this.commentId = jsonObject1.getInt("id");
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

    public String getUser_name_of_post() {
        return user_name_of_post;
    }

    public void setUser_name_of_post(String user_name_of_post) {
        this.user_name_of_post = user_name_of_post;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCredits_earned() {
        return credits_earned;
    }

    public void setCredits_earned(String credits_earned) {
        this.credits_earned = credits_earned;
    }

    public String getIs_Shared() {
        return is_Shared;
    }

    public void setIs_Shared(String is_Shared) {
        this.is_Shared = is_Shared;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getPaid_at() {
        return paid_at;
    }

    public void setPaid_at(String paid_at) {
        this.paid_at = paid_at;
    }

    public String getWeek_views() {
        return week_views;
    }

    public void setWeek_views(String week_views) {
        this.week_views = week_views;
    }

    public int getUser_post_id() {
        return user_post_id;
    }

    public void setUser_post_id(int user_post_id) {
        this.user_post_id = user_post_id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
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

    public int getSharedId() {
        return sharedId;
    }

    public void setSharedId(int sharedId) {
        this.sharedId = sharedId;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
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