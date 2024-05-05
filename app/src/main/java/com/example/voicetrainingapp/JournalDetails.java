package com.example.voicetrainingapp;

public class JournalDetails {

    private String entryText;//to hold text user wants to add to their journal
    private String date; //holds the date the entry was made
    private String email;//holds the users email
    private String rating;//holds the rating value
    private String id; //holds id for entry

    public JournalDetails(){
        this.entryText = entryText;
        this.date = date;
        this.rating = rating;
        this.email = email;
        this.id = id;
    }

    //setters and getters
    public String getEntryText() {
        return entryText;
    }

    public void setEntryText(String entryText) {
        this.entryText = entryText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetails() {
        return entryText + " - Date: " + date + " - Rating: " + rating;
    }
}
