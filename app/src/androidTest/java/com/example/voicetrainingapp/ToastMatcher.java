package com.example.voicetrainingapp;

import android.os.IBinder;
import androidx.test.espresso.Root;
import android.view.WindowManager;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
// https://stackoverflow.com/questions/28390574/checking-toast-message-in-android-espresso
@SuppressWarnings({"deprecation", "unchecked"})
public class ToastMatcher extends TypeSafeMatcher<Root> {

    @Override
    public void describeTo(Description description) {
        description.appendText("is toast");
    }

    public static Matcher<Root> isToast() {
        return new ToastMatcher();
    }

    @Override
    public boolean matchesSafely(Root root) {
        int type = root.getWindowLayoutParams().get().type;
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            IBinder windowToken = root.getDecorView().getWindowToken();
            IBinder appToken = root.getDecorView().getApplicationWindowToken();
            if (windowToken == appToken) {
                // windowToken == appToken means this window isn't contained by any other windows.
                // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
                return true;
            }
        }
        return false;
    }

}