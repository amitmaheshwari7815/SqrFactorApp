package com.hackerkernel.user.sqrfactor;

import java.io.Serializable;

public class comments_limited implements Serializable {

    public int comment_limited_id,comment_limited_user_id,comment_limited_commentable_id;
    public String comment_limited_commentable_type, comment_limited_body,comment_limited_created_at,comment_limited_updated_at;
    public String commentUserName,commentUserPrfile;
    public int likeCount;
    public UserClass userClass;

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String getCommentUserPrfile() {
        return commentUserPrfile;
    }

    public void setCommentUserPrfile(String commentUserPrfile) {
        this.commentUserPrfile = commentUserPrfile;
    }

    public String getComment_limited_commentable_type() {
        return comment_limited_commentable_type;

    }

    public UserClass getUserClass() {
        return userClass;
    }

    public void setUserClass(UserClass userClass) {
        this.userClass = userClass;
    }

    public void setComment_limited_commentable_type(String comment_limited_commentable_type) {
        this.comment_limited_commentable_type = comment_limited_commentable_type;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public comments_limited(int likeCount, String commentUserName, String commentUserPrfile, int comment_limited_id,
                            int comment_limited_user_id, int comment_limited_commentable_id, String comment_limited_commentable_type,
                            String comment_limited_body, String comment_limited_created_at,
                            String comment_limited_updated_at) {
        this.likeCount=likeCount;
        this.commentUserName=commentUserName;

        this.commentUserPrfile=commentUserPrfile;
        this.comment_limited_id = comment_limited_id;
        this.comment_limited_user_id = comment_limited_user_id;

        this.comment_limited_commentable_id = comment_limited_commentable_id;
        this.comment_limited_commentable_type=comment_limited_commentable_type;
        this.comment_limited_body = comment_limited_body;
        this.comment_limited_created_at = comment_limited_created_at;
        this.comment_limited_updated_at = comment_limited_updated_at;
    }

    public int getComment_limited_id() {
        return comment_limited_id;
    }

    public void setComment_limited_id(int comment_limited_id) {
        this.comment_limited_id = comment_limited_id;
    }

    public int getComment_limited_user_id() {
        return comment_limited_user_id;
    }

    public void setComment_limited_user_id(int comment_limited_user_id) {
        this.comment_limited_user_id = comment_limited_user_id;
    }

    public int getComment_limited_commentable_id() {
        return comment_limited_commentable_id;
    }

    public void setComment_limited_commentable_id(int comment_limited_commentable_id) {
        this.comment_limited_commentable_id = comment_limited_commentable_id;
    }

    public String getComment_limited_body() {
        return comment_limited_body;
    }

    public void setComment_limited_body(String comment_limited_body) {
        this.comment_limited_body = comment_limited_body;
    }

    public String getComment_limited_created_at() {
        return comment_limited_created_at;
    }

    public void setComment_limited_created_at(String comment_limited_created_at) {
        this.comment_limited_created_at = comment_limited_created_at;
    }

    public String getComment_limited_updated_at() {
        return comment_limited_updated_at;
    }

    public void setComment_limited_updated_at(String comment_limited_updated_at) {
        this.comment_limited_updated_at = comment_limited_updated_at;
    }
}
