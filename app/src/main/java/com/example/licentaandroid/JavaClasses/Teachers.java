package com.example.licentaandroid.JavaClasses;

public class Teachers {

    public String email, fullname, imageUrl, id;

    public Teachers(String email, String fullname, String imageUrl, String id) {
        this.email = email;
        this.fullname = fullname;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public Teachers() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
