package com.example.smartalec;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InstructorCourseActivity extends AppCompatActivity {
    private DatabaseReference database;
    private RecyclerView quizzesRecyclerView;
    private RecyclerView lecturesRecyclerView;
    private static String courseId;
    private String courseName;
    private String courseInstructors;
    private int color;
    private CardView courseCard;
    private MaterialLetterIcon courseIcon;
    private TextView courseNameTextView;
    private TextView courseInstructorsTextView;
    private TabHost tabHost;
    private Button newLectureButton;
    private Button newQuizButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_course);
        Intent intent = getIntent();
        courseId = intent.getStringExtra("courseId");
        courseName = intent.getStringExtra("courseName");
        courseInstructors = intent.getStringExtra("courseInstructors");
        color = intent.getIntExtra("color", 0);

        database = FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);

        courseCard = findViewById(R.id.courseCard);
        courseIcon = findViewById(R.id.courseIcon);
        courseNameTextView = findViewById(R.id.courseName);
        courseInstructorsTextView = findViewById(R.id.courseInstructors);
        courseCard.setElevation(0);
        courseIcon.setLetter(courseId.substring(0, 2) + "." + courseId.substring(2));
        courseIcon.setShapeColor(color);
        courseNameTextView.setText(courseName);
        courseInstructorsTextView.setText(courseInstructors);

        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Lectures");
        tabSpec.setContent(R.id.lecturesTab);
        tabSpec.setIndicator("Lectures");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Quizzes");
        tabSpec.setContent(R.id.quizzesTab);
        tabSpec.setIndicator("Quizzes");
        tabHost.addTab(tabSpec);

        // Lectures Tab ===============================================================================================

        final EditText newLectureTitle = new EditText(this);
        newLectureTitle.setHint("Lecture Title");

        final AlertDialog.Builder newLectureBuilder = new AlertDialog.Builder(this);
        newLectureBuilder.setTitle("New Lecture");
        newLectureBuilder.setView(newLectureTitle);
        newLectureBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                DatabaseReference newLectureRef = database.child("lectures").child(courseId).push();
                newLectureRef.setValue(new Lecture(newLectureRef.getKey(), newLectureTitle.getText().toString()));
                newLectureTitle.getText().clear();
                dialog.dismiss();
            }
        });
        final AlertDialog newLectureDialog = newLectureBuilder.create();
        newLectureButton = findViewById(R.id.newLectureButton);
        newLectureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newLectureDialog.show();
            }
        });

        // Quizzes Tab ================================================================================================

        final EditText newQuizTitle = new EditText(this);
        newQuizTitle.setHint("Quiz Title");

        final AlertDialog.Builder newQuizBuilder = new AlertDialog.Builder(this);
        newQuizBuilder.setTitle("New Quiz");
        newQuizBuilder.setView(newQuizTitle);
        newQuizBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                DatabaseReference newQuizRef = database.child("quizzes").child(courseId).push();
                newQuizRef.setValue(new Quiz(newQuizRef.getKey(), newQuizTitle.getText().toString()));
                newQuizTitle.getText().clear();
                dialog.dismiss();
            }
        });
        final AlertDialog newQuizDialog = newQuizBuilder.create();
        newQuizButton = findViewById(R.id.newQuizButton);
        newQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newQuizDialog.show();
            }
        });



        quizzesRecyclerView = findViewById(R.id.instructorQuizzesRecyclerView);
        LinearLayoutManager quizzesLayoutManager = new LinearLayoutManager(this);
        quizzesLayoutManager.setReverseLayout(true);
        quizzesLayoutManager.setStackFromEnd(true);
        quizzesRecyclerView.setLayoutManager(quizzesLayoutManager);
        lecturesRecyclerView = findViewById(R.id.instructorLecturesRecyclerView);
        LinearLayoutManager lecturesLayoutManager = new LinearLayoutManager(this);
        lecturesLayoutManager.setReverseLayout(true);
        lecturesLayoutManager.setStackFromEnd(true);
        lecturesRecyclerView.setLayoutManager(lecturesLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Lecture, InstructorLectureViewHolder> lectureRecyclerAdapter =
                new FirebaseRecyclerAdapter<Lecture, InstructorLectureViewHolder>(
                        Lecture.class,
                        R.layout.gp_row,
                        InstructorLectureViewHolder.class,
                        database.child("lectures").child(courseId)
                ) {
                    @Override
                    protected void populateViewHolder(InstructorLectureViewHolder viewHolder, Lecture lecture, int position) {
                        viewHolder.populate(lecture);
                    }
                };
        FirebaseRecyclerAdapter<Quiz, InstructorQuizViewHolder> quizRecyclerAdapter =
                new FirebaseRecyclerAdapter<Quiz, InstructorQuizViewHolder>(
                        Quiz.class,
                        R.layout.gp_row,
                        InstructorQuizViewHolder.class,
                        database.child("quizzes").child(courseId)
                ) {
                    @Override
                    protected void populateViewHolder(InstructorQuizViewHolder viewHolder, Quiz quiz, int position) {
                        viewHolder.populate(quiz);
                    }
                };
        quizzesRecyclerView.setAdapter(quizRecyclerAdapter);
        lecturesRecyclerView.setAdapter(lectureRecyclerAdapter);
    }

    public static class InstructorQuizViewHolder extends RecyclerView.ViewHolder {
        private String quizId;
        private String quizName;
        private View view;

        public InstructorQuizViewHolder(View view) {
            super(view);
            this.view = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Activity activity = (Activity) context;
                    Intent intent = new Intent(context, InstructorQuizActivity.class);
                    intent.putExtra("courseId", courseId);
                    intent.putExtra("quizId", quizId);
                    intent.putExtra("quizName", quizName);
                    context.startActivity(intent);
                }
            });
        }

        public void populate(Quiz quiz) {
            quizId = quiz.getId();
            quizName = quiz.getName();
            MaterialLetterIcon dateIcon = view.findViewById(R.id.gpRowIcon);
            TextView title = view.findViewById(R.id.gpTitle);
            dateIcon.setLetter("Quiz");
            dateIcon.setShapeColor(Color.argb(255, 175, 0, 0));
            title.setText(quizName);
        }
    }

    public static class InstructorLectureViewHolder extends RecyclerView.ViewHolder {
        private String lectureId;
        private String lectureName;
        private View view;

        public InstructorLectureViewHolder(View view) {
            super(view);
            this.view = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, InstructorLectureActivity.class);
                    intent.putExtra("courseId", courseId);
                    intent.putExtra("lectureId", lectureId);
                    intent.putExtra("lectureName", lectureName);
                    context.startActivity(intent);
                }
            });
        }

        public void populate(Lecture lecture) {
            MaterialLetterIcon dateIcon = view.findViewById(R.id.gpRowIcon);
            TextView title = view.findViewById(R.id.gpTitle);
            dateIcon.setLetter("Lecture");
            dateIcon.setShapeColor(Color.argb(255, 100, 100, 100));
            lectureId = lecture.getId();
            lectureName = lecture.getName();
            title.setText(lectureName);
        }
    }
}
