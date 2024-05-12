package com.example.voicetrainingapp;

import java.util.List;

public class OverallGraphCal {

    private int mascCount;
    private int femCount;
    private int androCount;

    public void sortFrequencies(List<Integer> frequencies) {
        for (int i = 0; i < frequencies.size(); i++) {
            if(frequencies.get(i) >= 85 && frequencies.get(i) <= 175) {
                mascCount++;
            }
            else if(frequencies.get(i) >= 147 && frequencies.get(i) <= 294) {
                femCount++;
            }
            else if(frequencies.get(i) >= 123 && frequencies.get(i) <= 247){
                androCount++;
            }
        }
    }

    public int getMascCount() {
        return mascCount;
    }

    public int getFemCount() {
        return femCount;
    }

    public int getAndroCount() {
        return androCount;
    }
}
