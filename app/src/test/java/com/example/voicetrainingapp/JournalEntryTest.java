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
@SuppressWarnings({"deprecation", "unchecked"})
public class JournalEntryTest {

    @Rule
    public ActivityTestRule<JournalEntry> activityRule = new ActivityTestRule<>(JournalEntry.class);

    @Before
    public void setUp() {
        Intents.init(); // Initialize Espresso Intents before each test
    }

    @After
    public void tearDown() {
        Intents.release(); // Release Espresso Intents after each test
    }

    @Test
    public void testSubmitButton_withValidInputs() {
        // Simulate user entering valid data
        onView(withId(R.id.rating))
                .perform(typeText("4"), closeSoftKeyboard());

        onView(withId(R.id.editText))
                .perform(typeText("Sample journal entry."), closeSoftKeyboard());

        // Click on the Submit button
        onView(withId(R.id.submitBtn)).perform(click());

        // Verify that the Journal activity is started
        Intents.intended(IntentMatchers.hasComponent(Journal.class.getName()));
    }

    @Test
    public void testCancelButton_navigatesToJournal() {
        // Click the cancel button
        onView(withId(R.id.cancelBtn)).perform(click());

        // Verify that the Journal activity is started
        Intents.intended(IntentMatchers.hasComponent(Journal.class.getName()));
    }
}
