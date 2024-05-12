package com.example.voicetrainingapp;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LoginTest {
    String DB_EMAIL = "example@example.com";
    String DB_PASSWORD = "Password1";

    // Methods to validate credentials
    private boolean validateEmail(String inputEmail) {
        return DB_EMAIL.equals(inputEmail);
    }

    private boolean validatePassword(String inputPassword) {
        return DB_PASSWORD.equals(inputPassword);
    }

    @Test
    public void invalidDetails() {
        assertTrue("The input email should match the expected email.",
                validateEmail("example@example.com"));
        assertFalse("The input password should not match the expected password.",
                validatePassword("password"));
    }

    @Test
    public void validDetails() {
        assertTrue("The input email should match the expected email.",
                validateEmail("example@example.com"));
        assertTrue("The input password should  match the expected password.",
                validatePassword("Password1"));
    }
}
