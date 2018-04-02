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
import com.google.firebase.database.*;

import java.util.Arrays;
import java.util.List;

public class InstructorQuizActivity extends AppCompatActivity {
    private DatabaseReference database;
    private RecyclerView quizScoresRecyclerView;
    private String courseId;
    private String quizId;
    private String quizName;
    private CardView gpCard;
    private MaterialLetterIcon gpRowIcon;
    private TextView gpTitle;
    private TabHost tabHost;
    private EditText editQuizEditText;
    private Button editQuizActiveButton;
    private Button editQuizSaveButton;
    private boolean active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_quiz);
        Intent intent = getIntent();
        courseId = intent.getStringExtra("courseId");
        quizId = intent.getStringExtra("quizId");
        quizName = intent.getStringExtra("quizName");

        database = FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);

        gpCard = findViewById(R.id.gpCard);
        gpRowIcon = findViewById(R.id.gpRowIcon);
        gpTitle = findViewById(R.id.gpTitle);
        gpCard.setElevation(0);
        gpRowIcon.setLetter("Quiz");
        gpRowIcon.setShapeColor(Color.argb(255, 175, 0, 0));
        gpTitle.setText(quizName);

        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Edit Quiz");
        tabSpec.setContent(R.id.editQuizTab);
        tabSpec.setIndicator("Edit Quiz");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Scores");
        tabSpec.setContent(R.id.quizScoresTab);
        tabSpec.setIndicator("Scores");
        tabHost.addTab(tabSpec);

        // Edit Quiz Tab ==============================================================================================
        editQuizEditText = findViewById(R.id.instructorEditQuizEditText);
        editQuizActiveButton = findViewById(R.id.instructorEditQuizActiveButton);
        editQuizSaveButton = findViewById(R.id.instructorEditQuizSaveButton);

        editQuizActiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child("quizzes").child(courseId).child(quizId).child("active").setValue(!active);
                if (active) {
                    Toast.makeText(InstructorQuizActivity.this, "Set quiz to inactive", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InstructorQuizActivity.this, "Set quiz to active", Toast.LENGTH_SHORT).show();
                }
            }
        });

        database.child("quizzes").child(courseId).child(quizId).child("active").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                active = dataSnapshot.getValue(Boolean.class);
                if (active) {
                    editQuizActiveButton.setText("Set Inactive");
                } else {
                    editQuizActiveButton.setText("Set Active");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        editQuizSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] questionArr = editQuizEditText.getText().toString().split("~");
                database.child("quizzes").child(courseId).child(quizId).child("questions").setValue(Arrays.asList(questionArr));
                Toast.makeText(InstructorQuizActivity.this, "Saved Quiz", Toast.LENGTH_SHORT).show();
            }
        });

        database.child("quizzes").child(courseId).child(quizId).child("questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> questionsList = (List<String>) dataSnapshot.getValue();
                if (questionsList != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < questionsList.size(); i++) {
                        sb.append(questionsList.get(i));
                        if (i != questionsList.size() - 1) {
                            sb.append("~");
                        }
                    }
                    editQuizEditText.setText(sb.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // Scores Tab =================================================================================================
        quizScoresRecyclerView = findViewById(R.id.instructorQuizScoresRecyclerView);
        quizScoresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Score, InstructorQuizScoresViewHolder> scoreRecyclerAdapter =
                new FirebaseRecyclerAdapter<Score, InstructorQuizScoresViewHolder>(
                        Score.class,
                        R.layout.quiz_score_row,
                        InstructorQuizScoresViewHolder.class,
                        database.child("scores").child(courseId).child(quizId)
                ) {
                    @Override
                    protected void populateViewHolder(InstructorQuizScoresViewHolder viewHolder, Score score, int position) {
                        viewHolder.populate(score);
                    }
                };
        quizScoresRecyclerView.setAdapter(scoreRecyclerAdapter);
    }

    public static class InstructorQuizScoresViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public InstructorQuizScoresViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public void populate(Score score) {
            TextView studentId = view.findViewById(R.id.quizScoreId);
            TextView studentName = view.findViewById(R.id.quizScoreName);
            TextView studentScore = view.findViewById(R.id.quizScoreScore);
            studentId.setText(score.getStudentId());
            studentName.setText(score.getStudentName());
            studentScore.setText(String.valueOf(score.getScore()));
        }
    }
}
