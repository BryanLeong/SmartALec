package com.example.smartalec;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class StudentQuizActivity extends AppCompatActivity {
    private DatabaseReference database;
    private RecyclerView questionRecyclerView;
    private String courseId;
    private String courseName;
    private String courseInstructors;
    private int color;
    private String quizId;
    private String quizName;
    private CardView gpCard;
    private MaterialLetterIcon gpRowIcon;
    private TextView gpTitle;
    private Button submitButton;
    private static List<String> correctAnswers = new ArrayList<>();
    private String studentId;
    private String studentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quiz);
        Intent intent = getIntent();
        courseId = intent.getStringExtra("courseId");
        courseName = intent.getStringExtra("courseName");
        courseInstructors = intent.getStringExtra("courseInstructors");
        color = intent.getIntExtra("color", 0);
        quizId = intent.getStringExtra("quizId");
        quizName = intent.getStringExtra("quizName");

        database = FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);

        gpCard = findViewById(R.id.gpCard);
        gpRowIcon = findViewById(R.id.gpRowIcon);
        gpTitle = findViewById(R.id.gpTitle);
        gpCard.setElevation(0);
        gpRowIcon.setShapeColor(Color.argb(255, 175, 0, 0));
        gpRowIcon.setLetter("Quiz");
        gpTitle.setText(quizName);

        questionRecyclerView = findViewById(R.id.questionRecyclerView);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database.child("users").child(studentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentName = dataSnapshot.getValue(User.class).getName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        submitButton = findViewById(R.id.quizSubmitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child("scores").child(courseId).child(quizId).child(studentId)
                        .setValue(new Score(studentId, studentName, correctAnswers.size()));
                database.child("quizzes").child(courseId).child(quizId).child("completed").push().setValue(studentId);
                Toast.makeText(StudentQuizActivity.this,
                        "Answers Submitted! You got " + correctAnswers.size() + " question(s) correct",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<String, QuestionViewHolder> questionRecyclerAdapter =
                new FirebaseRecyclerAdapter<String, QuestionViewHolder>(
                        String.class,
                        R.layout.question_row,
                        QuestionViewHolder.class,
                        database.child("quizzes").child(courseId).child(quizId).child("questions")
                ) {
                    @Override
                    protected void populateViewHolder(QuestionViewHolder viewHolder, String string, int position) {
                        viewHolder.populate(string);
                    }
                };
        questionRecyclerView.setAdapter(questionRecyclerAdapter);
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        private String question;
        private List<String> options;
        private ArrayAdapter<String> adapter;
        private TextView quizQuestionTextView;
        private Spinner quizQuestionSpinner;
        private View view;
        private List<String> answers;

        public QuestionViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public void populate(String rawQuestion) {
            String[] rawArr = rawQuestion.split(";");
            question = rawArr[0];
            options = new ArrayList<>();
            answers = new ArrayList<>();
            for (int i = 1; i < rawArr.length; i++) {
                String option = rawArr[i].replace("@", "");
                if (rawArr[i].contains("@")) {
                    answers.add(option);
                }
                options.add(option);
            }
            adapter = new ArrayAdapter<>(view.getContext(), R.layout.support_simple_spinner_dropdown_item, options);

            quizQuestionTextView = view.findViewById(R.id.quizQuestionTextView);
            quizQuestionTextView.setText(question);
            quizQuestionSpinner = view.findViewById(R.id.quizQuestionSpinner);
            quizQuestionSpinner.setAdapter(adapter);
            quizQuestionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String selected = (String) adapterView.getSelectedItem();
                    if (answers.contains(selected)) {
                        correctAnswers.add(String.valueOf(getAdapterPosition()));
                    } else {
                        if (correctAnswers.contains(String.valueOf(getAdapterPosition()))) {
                            correctAnswers.remove(String.valueOf(getAdapterPosition()));
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }
}
