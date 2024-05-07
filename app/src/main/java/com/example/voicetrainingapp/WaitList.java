package com.example.voicetrainingapp;

public class WaitList {
    String date;
    boolean OnList;

    public WaitList(){

    }

    public WaitList(String date, boolean onList) {
        this.date = date;
        OnList = onList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isOnList() {
        return OnList;
    }

    public void setOnList(boolean onList) {
        OnList = onList;
    }
}
