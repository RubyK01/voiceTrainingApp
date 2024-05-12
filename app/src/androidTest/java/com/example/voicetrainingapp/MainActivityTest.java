package com.example.voicetrainingapp;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Before
    public void setUp() {
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testLogoutButton() {
        // Check that the logout button is displayed
        onView(withId(R.id.logout)).check(matches(isDisplayed()));

        // Click the logout button
        onView(withId(R.id.logout)).perform(ViewActions.click());
    }

    @Test
    public void testJournalButton() {
        // Check that the Journal button is displayed
        onView(withId(R.id.button_second)).check(matches(isDisplayed()));

        // Click the Journal button
        onView(withId(R.id.button_second)).perform(ViewActions.click());
    }

    @Test
    public void testFabWaitlistMessage() {
        // Click the FAB button
        onView(withId(R.id.fab)).perform(ViewActions.click());

        // Check for Snackbar with waitlist message
        onView(withText("You've been added to the wait list for a speech therapist, expect an email soon!"))
                .check(matches(isDisplayed()));
    }
}
