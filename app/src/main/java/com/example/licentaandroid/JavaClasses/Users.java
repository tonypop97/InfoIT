package com.example.licentaandroid.JavaClasses;


import com.google.firebase.database.Exclude;

public class Users {
    public String fullName, email, imageUrl, uid;


    public Users() {
    }


    public Users(String fullName, String email, String uid) {
        this.fullName = fullName;
        this.email = email;
        this.uid = uid;
    }

    public Users(String fullName, String email, String imageUrl, String uid) {
        this.fullName = fullName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}



