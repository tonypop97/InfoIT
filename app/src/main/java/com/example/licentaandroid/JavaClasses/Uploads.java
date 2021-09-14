package com.example.licentaandroid.JavaClasses;

public class Uploads {
    private String idUsers, fname, url, email;

    public Uploads() {
    }

    public Uploads(String idUsers, String fname, String url, String email) {
        this.idUsers = idUsers;
        this.fname = fname;
        this.url = url;
        this.email = email;
    }

    public String getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(String idUsers) {
        this.idUsers = idUsers;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
