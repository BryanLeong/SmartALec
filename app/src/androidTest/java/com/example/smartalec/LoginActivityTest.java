package com.example.smartalec;

import android.os.SystemClock;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test //Success Case: Valid Email or Password
    public void TestValidEmailAndPassword() {
        String email = "weian_low@mymail.sutd.edu.sg";
        String password = "Password";

        //Find the email/password edit text and type in the email/password
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());

        //Click on the login button
        onView(withId(R.id.loginButton)).perform(click());

        //Wait for toast message to appear
        SystemClock.sleep(2000);

        //Check whether toast message "Authentication Successful" appears
        LoginActivity activity = mActivityTestRule.getActivity();
        onView(withText("Authentication Successful")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).
                check(matches(isDisplayed()));
        SystemClock.sleep(3000);
    }

    @Test //Failure Case 1: Invalid Email
    public void TestInvalidEmail() {
        String email = "@@@@@mymail.sutd.edu.sg";
        String password = "Password";

        //Find the email/password edit text and type in the email/password
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());

        //Click on the login button
        onView(withId(R.id.loginButton)).perform(click());

        //Wait for toast message to appear
        SystemClock.sleep(2000);

        //Check whether toast message "Authentication failed" appears
        LoginActivity activity = mActivityTestRule.getActivity();
        onView(withText("Authentication failed")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).
                check(matches(isDisplayed()));
        SystemClock.sleep(3000);
    }

    @Test //Failure Case 2: Invalid Password
    public void TestInvalidPassword() {
        String email = "weian_low@mymail.sutd.edu.sg";
        String password = "Pass1234";

        //Find the email/password edit text and type in the email/password
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());

        //Click on the login button
        onView(withId(R.id.loginButton)).perform(click());

        //Wait for toast message to appear
        SystemClock.sleep(2000);

        //Check whether toast message "Authentication failed" appears
        LoginActivity activity = mActivityTestRule.getActivity();
        onView(withText("Authentication failed")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).
                check(matches(isDisplayed()));
        SystemClock.sleep(3000);
    }

    @Test //Failure Case 3: Invalid Email and Password
    public void TestInvalidEmailAndPassword() {
        String email = "@@@@@mymail.sutd.edu.sg";
        String password = "Pass1234";

        //Find the email/password edit text and type in the email/password
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());

        //Click on the login button
        onView(withId(R.id.loginButton)).perform(click());

        //Wait for toast message to appear
        SystemClock.sleep(2000);

        //Check whether toast message "Authentication failed" appears
        LoginActivity activity = mActivityTestRule.getActivity();
        onView(withText("Authentication failed")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).
                check(matches(isDisplayed()));

        SystemClock.sleep(3000);
    }

    @Test //Failure Case 4: Empty Email
    public void TestEmptyEmail() {
        String password = "Password";

        //Find the password edit text and type in the password
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());

        //Click on the login button
        onView(withId(R.id.loginButton)).perform(click());

        //Check whether toast message "Error: Email field is empty!" appears
        LoginActivity activity = mActivityTestRule.getActivity();
        onView(withText("Error: Email field is empty!")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).
                check(matches(isDisplayed()));
        SystemClock.sleep(3000);
    }

    @Test //Failure Case 5: Empty Password
    public void TestEmptyPassword() {
        String email = "weian_low@mymail.sutd.edu.sg";

        //Find the email edit text and type in the password
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());

        //Click on the login button
        onView(withId(R.id.loginButton)).perform(click());

        //Check whether toast message "Error: Password field is empty!" appears
        LoginActivity activity = mActivityTestRule.getActivity();
        onView(withText("Error: Password field is empty!")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).
                check(matches(isDisplayed()));
        SystemClock.sleep(3000);
    }

    @Test //Failure Case 6: Empty Email and Password
    public void TestEmptyEmailAndPassword() {

        //Click on the login button
        onView(withId(R.id.loginButton)).perform(click());

        //Check whether toast message "Error: Email and password fields are empty!" appears
        LoginActivity activity = mActivityTestRule.getActivity();
        onView(withText("Error: Email and password fields are empty!")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).
                check(matches(isDisplayed()));
        SystemClock.sleep(3000);
    }
}