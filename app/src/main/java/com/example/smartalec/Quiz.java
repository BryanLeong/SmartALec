package com.example.smartalec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quiz {
    private String id;
    private String name;
    private boolean active;
    private List<String> questions;
    private Map<String, String> completed;

    public Quiz() {
    }

    public Quiz(String id, String name) {
        this.id = id;
        this.name = name;
        active = false;
        questions = new ArrayList<>();
        completed = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public Map<String, String> getCompleted() {
        return completed;
    }

    public void setCompleted(Map<String, String> completed) {
        this.completed = completed;
    }

    public boolean checkCompleted(String studentId) {
        return completed != null && completed.values().contains(studentId);
    }
}
