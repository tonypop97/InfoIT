package com.example.licentaandroid.JavaClasses;

import android.net.Uri;

import com.google.firebase.database.ServerValue;


public class Comment {
    private String username, content, userID, imageUrl;
    private Object timestamp;

    public Comment(String content, String username, String userID, String imageUrl) {
        this.username = username;
        this.content = content;
        this.userID = userID;
        this.imageUrl = imageUrl;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public Comment() {
    }

    public Comment(String content, String username, String userID, String imageUrl, Object timestamp) {
        this.username = username;
        this.content = content;
        this.userID = userID;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String uid) {
        this.userID = uid;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

