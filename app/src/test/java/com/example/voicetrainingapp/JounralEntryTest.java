package com.example.voicetrainingapp;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

public class JounralEntryTest {
    private EntryLogic validator = new EntryLogic();

    @Test
    public void ratingMustBeBetween1And5() {
        assertEquals("Rating must be a number between 1 - 5!", validator.validateEntry("0", "Valid text"));
        assertEquals("Rating must be a number between 1 - 5!", validator.validateEntry("6", "Valid text"));
    }

    @Test
    public void entryCannotBeTooLong() {
        String a = "a";
        String bunchOfA ="";
        for (int i = 0; i < 252; i++){
            bunchOfA = bunchOfA + a;
        }
        assertEquals("Entries cannot be greater than 250 characters!", validator.validateEntry("5", bunchOfA));
    }

    @Test
    public void entryMustHaveText() {
        assertEquals("Entries must have text!", validator.validateEntry("5", ""));
    }

    @Test
    public void validEntry() {
        assertEquals("Valid", validator.validateEntry("5", "This is a valid entry"));
    }
}
