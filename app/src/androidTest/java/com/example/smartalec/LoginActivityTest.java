package com.example.smartalec;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.*;

public class LoginActivityTest {
    private Activity activity;
    private Activity newActivity;
    private Instrumentation.ActivityMonitor activityMonitor;

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        activity = activityTestRule.getActivity();
        activityMonitor = getInstrumentation().addMonitor(CourseListActivity.class.getName(), null, false);
    }

    @Test
    public void testViewsValid() {
        assertNotNull(activity.findViewById(R.id.email));
        assertNotNull(activity.findViewById(R.id.password));
        assertNotNull(activity.findViewById(R.id.resetPasswordButton));
        assertNotNull(activity.findViewById(R.id.loginButton));
    }

    @Test
    public void testLoginInvalidAccount() {
        onView(withId(R.id.email)).perform(typeText("invalid@account.com"));
        onView(withId(R.id.password)).perform(typeText("Password"));
        onView(withId(R.id.loginButton)).perform(click());
        newActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNull(newActivity);
    }

    @Test
    public void testLoginValidAccount() {
        onView(withId(R.id.email)).perform(typeText("test@account.com"));
        onView(withId(R.id.password)).perform(typeText("Password"));
        onView(withId(R.id.loginButton)).perform(click());
        newActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(newActivity);
    }

    @After
    public void tearDown() throws Exception {
        activity.finish();
        if (newActivity != null) {
            newActivity.finish();
        }
        activity = null;
        newActivity = null;
        activityMonitor = null;
    }
}