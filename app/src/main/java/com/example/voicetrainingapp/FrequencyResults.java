package com.example.voicetrainingapp;

import java.util.ArrayList;

public class FrequencyResults {
    private ArrayList<Double> firstSoundResults; //holds the frequencies for the first sound.
    private ArrayList<Double> secondSoundResults; //holds the frequencies for the second sound.
    private ArrayList<Double> thirdSoundResults;//holds the frequencies for the third sound.
    private String email;
    private String date;

    public FrequencyResults(){

    }

    public ArrayList<Double> getFirstSoundResults() {
        return firstSoundResults;
    }

    public void setFirstSoundResults(ArrayList<Double> firstSoundResults) {
        this.firstSoundResults = firstSoundResults;
    }

    public ArrayList<Double> getSecondSoundResults() {
        return secondSoundResults;
    }

    public void setSecondSoundResults(ArrayList<Double> secondSoundResults) {
        this.secondSoundResults = secondSoundResults;
    }

    public ArrayList<Double> getThirdSoundResults() {
        return thirdSoundResults;
    }

    public void setThirdSoundResults(ArrayList<Double> thirdSoundResults) {
        this.thirdSoundResults = thirdSoundResults;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
