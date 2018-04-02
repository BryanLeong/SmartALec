package com.example.smartalec;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentLectureActivity extends AppCompatActivity {
    private DatabaseReference database;
    private String courseId;
    private String lectureId;
    private String lectureName;
    private CardView gpCard;
    private MaterialLetterIcon gpRowIcon;
    private TextView gpTitle;
    private EditText askQuestionEditText;
    private EditText leaveFeedbackEditText;
    private Button askQuestionButton;
    private Button leaveFeedbackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_lecture);
        Intent intent = getIntent();
        courseId = intent.getStringExtra("courseId");
        lectureId = intent.getStringExtra("lectureId");
        lectureName = intent.getStringExtra("lectureName");

        database = FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);

        gpCard = findViewById(R.id.gpCard);
        gpRowIcon = findViewById(R.id.gpRowIcon);
        gpTitle = findViewById(R.id.gpTitle);
        gpCard.setElevation(0);
        gpRowIcon.setLetter("Lecture");
        gpRowIcon.setShapeColor(Color.argb(255, 100, 100, 100));
        gpTitle.setText(lectureName);

        askQuestionEditText = findViewById(R.id.askQuestionEditText);
        leaveFeedbackEditText = findViewById(R.id.leaveFeedbackEditText);
        askQuestionButton = findViewById(R.id.askQuestionButton);
        leaveFeedbackButton = findViewById(R.id.leaveFeedbackButton);

        askQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child("lectures").child(courseId).child(lectureId)
                        .child("questions").push().setValue(askQuestionEditText.getText().toString());
                askQuestionEditText.getText().clear();
                Toast.makeText(StudentLectureActivity.this, "Question submitted", Toast.LENGTH_SHORT).show();
            }
        });

        leaveFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child("lectures").child(courseId).child(lectureId)
                        .child("feedback").push().setValue(leaveFeedbackEditText.getText().toString());
                leaveFeedbackEditText.getText().clear();
                Toast.makeText(StudentLectureActivity.this, "Feedback submitted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
