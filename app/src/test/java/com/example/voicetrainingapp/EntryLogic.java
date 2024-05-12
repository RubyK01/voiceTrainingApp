package com.example.voicetrainingapp;

public class EntryLogic {
    public String validateEntry(String rating, String entryText) {
        if (!rating.matches("[1-5]")) {
            return "Rating must be a number between 1 - 5!";
        } else if (entryText.length() > 250) {
            return "Entries cannot be greater than 250 characters!";
        } else if (entryText.isEmpty()) {
            return "Entries must have text!";
        } else {
            return "Valid";
        }
    }
}
