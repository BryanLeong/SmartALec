package com.example.smartalec;

public class Score {
    private String studentId;
    private String studentName;
    private int score;

    public Score() {
    }

    public Score(String studentId, String studentName, int score) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.score = score;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
