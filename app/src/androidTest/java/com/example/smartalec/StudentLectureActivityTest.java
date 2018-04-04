package com.example.smartalec;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.contrib.RecyclerViewActions;
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

public class StudentLectureActivityTest {
    private Activity activity1;
    private Activity activity2;
    private Instrumentation.ActivityMonitor courseListActivityMonitor;
    private Instrumentation.ActivityMonitor studentCourseActivityMonitor;
    private Instrumentation.ActivityMonitor studentLectureActivityMonitor;

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        activity1 = activityTestRule.getActivity();
        courseListActivityMonitor = getInstrumentation().addMonitor(CourseListActivity.class.getName(), null, false);
        studentCourseActivityMonitor = getInstrumentation().addMonitor(StudentCourseActivity.class.getName(), null, false);
        studentLectureActivityMonitor = getInstrumentation().addMonitor(StudentLectureActivity.class.getName(), null, false);
    }

    @Test
    public void testSubmitQuestionFeedback() throws Exception {
        onView(withId(R.id.email)).perform(typeText("test@account.com"));
        onView(withId(R.id.password)).perform(typeText("Password"));
        onView(withId(R.id.loginButton)).perform(click());
        activity2 = getInstrumentation().waitForMonitorWithTimeout(courseListActivityMonitor, 5000);
        assertNotNull(activity2);
        activity1.finish();
        Thread.sleep(5000);
        onView(withId(R.id.courseRecyclerView)).perform(RecyclerViewActions.<CourseListActivity.CourseViewHolder>actionOnItemAtPosition(1, click()));
        activity1 = getInstrumentation().waitForMonitorWithTimeout(studentCourseActivityMonitor, 5000);
        assertNotNull(activity1);
        activity2.finish();
        Thread.sleep(5000);
        onView(withId(R.id.studentLecturesRecyclerView)).perform(RecyclerViewActions.<StudentCourseActivity.StudentLectureViewHolder>actionOnItemAtPosition(1, click()));
        activity2 = getInstrumentation().waitForMonitorWithTimeout(studentLectureActivityMonitor, 5000);
        assertNotNull(activity2);
        activity1.finish();
        onView(withId(R.id.askQuestionEditText)).perform(typeText("Is this a question?"));
        Thread.sleep(500);
        onView(withId(R.id.askQuestionButton)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.leaveFeedbackEditText)).perform(typeText("This is real feedback"));
        Thread.sleep(500);
        onView(withId(R.id.leaveFeedbackButton)).perform(click());
        Thread.sleep(1000);
    }

    @After
    public void tearDown() throws Exception {
        if (activity1 != null) {
            activity1.finish();
        }
        if (activity2 != null) {
            activity2.finish();
        }
        activity1 = null;
        activity2 = null;
        courseListActivityMonitor = null;
        studentCourseActivityMonitor = null;
        studentLectureActivityMonitor = null;
    }
}