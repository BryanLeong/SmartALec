package com.example.smartalec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InstructorLectureActivity extends AppCompatActivity {
    private DatabaseReference database;
    private RecyclerView questionsRecyclerView;
    private RecyclerView feedbackRecyclerView;
    private String courseId;
    private String lectureId;
    private String lectureName;
    private CardView gpCard;
    private MaterialLetterIcon gpRowIcon;
    private TextView gpTitle;
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_lecture);
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

        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Questions");
        tabSpec.setContent(R.id.questionsTab);
        tabSpec.setIndicator("Questions");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Feedback");
        tabSpec.setContent(R.id.feedbackTab);
        tabSpec.setIndicator("Feedback");
        tabHost.addTab(tabSpec);

        questionsRecyclerView = findViewById(R.id.instructorQuestionsRecyclerView);
        LinearLayoutManager questionsLayoutManager = new LinearLayoutManager(this);
        questionsLayoutManager.setReverseLayout(true);
        questionsLayoutManager.setStackFromEnd(true);
        questionsRecyclerView.setLayoutManager(questionsLayoutManager);
        feedbackRecyclerView = findViewById(R.id.instructorFeedbackRecyclerView);
        LinearLayoutManager feedbackLayoutManager = new LinearLayoutManager(this);
        feedbackLayoutManager.setReverseLayout(true);
        feedbackLayoutManager.setStackFromEnd(true);
        feedbackRecyclerView.setLayoutManager(feedbackLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<String, InstructorTextViewHolder> questionsRecyclerAdapter =
                new FirebaseRecyclerAdapter<String, InstructorTextViewHolder>(
                        String.class,
                        R.layout.text_row,
                        InstructorTextViewHolder.class,
                        database.child("lectures").child(courseId).child(lectureId).child("questions")
                ) {
                    @Override
                    protected void populateViewHolder(InstructorTextViewHolder viewHolder, String string, int position) {
                        viewHolder.populate(string);
                    }
                };
        FirebaseRecyclerAdapter<String, InstructorTextViewHolder> feedbackRecyclerAdapter =
                new FirebaseRecyclerAdapter<String, InstructorTextViewHolder>(
                        String.class,
                        R.layout.text_row,
                        InstructorTextViewHolder.class,
                        database.child("lectures").child(courseId).child(lectureId).child("feedback")
                ) {
                    @Override
                    protected void populateViewHolder(InstructorTextViewHolder viewHolder, String string, int position) {
                        viewHolder.populate(string);
                    }
                };
        questionsRecyclerView.setAdapter(questionsRecyclerAdapter);
        feedbackRecyclerView.setAdapter(feedbackRecyclerAdapter);
    }

    public static class InstructorTextViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public InstructorTextViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public void populate(String string) {
            TextView textRowTextView = view.findViewById(R.id.textRowTextView);
            textRowTextView.setText(string);
        }
    }
}
