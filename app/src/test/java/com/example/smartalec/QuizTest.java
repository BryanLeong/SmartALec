package com.example.smartalec;

import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class QuizTest {
    private Quiz quiz;

    @Test
    public void testCheckCompletedNullMap() {
        quiz = new Quiz();
        assertFalse(quiz.checkCompleted("testId"));
    }

    @Test
    public void testCheckCompletedNonNullMap() {
        quiz = new Quiz("testQuiz", "Test Quiz");
        assertFalse(quiz.checkCompleted("testId"));
        Map<String, String> completedMap = new HashMap<>();
        completedMap.put("key", "testId");
        quiz.setCompleted(completedMap);
        assertTrue(quiz.checkCompleted("testId"));
    }

    @After
    public void tearDown() throws Exception {
        quiz = null;
    }
}