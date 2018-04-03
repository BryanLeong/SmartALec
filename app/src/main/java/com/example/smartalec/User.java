package com.example.smartalec;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String type;
    private List<String> courses;

    public User() {
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.type = "student";
        courses = new ArrayList<>();
    }

    public User(String id, String name, String type, List<String> courses) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.courses = courses;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }
}
