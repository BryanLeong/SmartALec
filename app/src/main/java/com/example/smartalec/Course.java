package com.example.smartalec;

public class Course {
    private String id;
    private String name;
    private String instructors;

    public Course() {
    }

    public Course(String id, String name, String instructors) {
        this.id = id;
        this.name = name;
        this.instructors = instructors;
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

    public String getInstructors() {
        return instructors;
    }

    public void setInstructors(String instructors) {
        this.instructors = instructors;
    }
}
