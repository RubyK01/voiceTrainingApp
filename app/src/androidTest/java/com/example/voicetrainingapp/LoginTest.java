package com.example.voicetrainingapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@SuppressWarnings({"deprecation", "unchecked"})
@RunWith(JUnit4.class)
public class LoginTest {
    // https://www.youtube.com/watch?v=J5bHxbsITDM
//    @Rule
//    public IntentsTestRule<Login> intentsTestRule = new IntentsTestRule<>(Login.class);
    @Rule
    public ActivityScenarioRule<Login> activityScenarioRule = new ActivityScenarioRule<>(Login.class);
    private View decorView;

    @Before
    public void setup() {
        activityScenarioRule.getScenario().onActivity(activity -> decorView = activity.getWindow().getDecorView());
    }


    @After
    public void tearDown() {
//        Intents.release();
    }
    @Test
    public void testElementsDisplayed() {
        onView(withId(R.id.email)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.loginNow)).check(matches(isDisplayed()));
    }

    @Test
    public void testSuccessfulLogin() {
        onView(withId(R.id.email)).perform(typeText("example@example.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());
        isToastMessageDisplayed("Logged in!"); // Directly use the expected toast message
    }

    public void isToastMessageDisplayed(String message) {
        onView(withText(message))
                .inRoot(MobileViewMatchers.isToast())
                .check(matches(isDisplayed()));
    }


    @Test
    public void testLoginWithEmptyFields() {
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withText("Email required.")).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithEmptyPasswordField() {
        onView(withId(R.id.email)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());
        intended(anyIntent());
    }
}
