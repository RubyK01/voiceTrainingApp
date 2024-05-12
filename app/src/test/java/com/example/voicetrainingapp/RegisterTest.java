package com.example.voicetrainingapp;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import org.junit.Test;

public class RegisterTest {

    private RegisterLogic RegisterTestLogic = new RegisterLogic();

    @Test
    public void emailIsRequired() {
        assertEquals("Email required.", RegisterTestLogic.validDetails("", "Password1!"));
    }

    @Test
    public void invalidEmailFormat() {
        assertEquals("Invalid email format.", RegisterTestLogic.validDetails("example.com", "Password1!"));
    }

    @Test
    public void passwordIsRequired() {
        assertEquals("Password required.", RegisterTestLogic.validDetails("example@example.com", ""));
    }

    @Test
    public void passwordNotValid() {
        assertEquals("Password not valid.", RegisterTestLogic.validDetails("example@example.com", "pass"));
    }

    @Test
    public void validDetails() {
        assertEquals("Valid", RegisterTestLogic.validDetails("example@example.com", "Password1!"));
    }
}
