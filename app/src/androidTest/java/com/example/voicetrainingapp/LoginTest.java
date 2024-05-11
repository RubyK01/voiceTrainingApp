package com.example.voicetrainingapp;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.espresso.intent.Intents;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.view.View;

import com.example.voicetrainingapp.databinding.FragmentFirstBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

@SuppressWarnings({"deprecation", "unchecked"})
@RunWith(JUnit4.class)
public class LoginTest {
    // https://www.youtube.com/watch?v=J5bHxbsITDM
    @Rule
    public ActivityTestRule<Login> activityRule = new ActivityTestRule<>(Login.class);

    @Before
    public void setup() {
        Intents.init();
//        MockitoAnnotations.initMocks(this);
//        FirebaseAuth mockFirebaseAuth = FirebaseAuth.getInstance();
//        when(mockFirebaseAuth.signInWithEmailAndPassword(anyString(), anyString()))
//                .thenReturn(Tasks.forResult(mock(AuthResult.class)));  // Simulate successful authentication
    }


    @After
    public void tearDown() {
        Intents.release();
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
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void testLoginWithEmptyFields() {
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withText("Email required."))
                .inRoot(withDecorView(not((View) is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithMissingPassword() {
        onView(withId(R.id.email)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withText("Password required.")).check(matches(isDisplayed()));
    }
}
