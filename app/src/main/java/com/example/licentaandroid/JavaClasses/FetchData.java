package com.example.licentaandroid.JavaClasses;

public class FetchData {
    public String id, title, text, homework;

    public FetchData(String id, String title, String text, String homework) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.homework = homework;
    }

    public FetchData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }
}
