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
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentCourseActivity extends AppCompatActivity {
    private DatabaseReference database;
    private RecyclerView quizRecyclerView;
    private RecyclerView lectureRecyclerView;
    private static String courseId;
    private static String courseName;
    private static String courseInstructors;
    private static int color;
    private CardView courseCard;
    private MaterialLetterIcon courseIcon;
    private TextView courseNameTextView;
    private TextView courseInstructorsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course);
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

        quizRecyclerView = findViewById(R.id.studentQuizzesRecyclerView);
        LinearLayoutManager quizzesLayoutManager = new LinearLayoutManager(this);
        quizzesLayoutManager.setReverseLayout(true);
        quizzesLayoutManager.setStackFromEnd(true);
        quizRecyclerView.setLayoutManager(quizzesLayoutManager);
        lectureRecyclerView = findViewById(R.id.studentLecturesRecyclerView);
        LinearLayoutManager lecturesLayoutManager = new LinearLayoutManager(this);
        lecturesLayoutManager.setReverseLayout(true);
        lecturesLayoutManager.setStackFromEnd(true);
        lectureRecyclerView.setLayoutManager(lecturesLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Quiz, StudentQuizViewHolder> quizRecyclerAdapter =
                new FirebaseRecyclerAdapter<Quiz, StudentQuizViewHolder>(
                        Quiz.class,
                        R.layout.gp_row,
                        StudentQuizViewHolder.class,
                        database.child("quizzes").child(courseId)
                ) {
                    @Override
                    protected void populateViewHolder(StudentQuizViewHolder viewHolder, Quiz quiz, int position) {
                        viewHolder.populate(quiz);
                    }
                };
        FirebaseRecyclerAdapter<Lecture, StudentLectureViewHolder> lectureRecyclerAdapter =
                new FirebaseRecyclerAdapter<Lecture, StudentLectureViewHolder>(
                        Lecture.class,
                        R.layout.gp_row,
                        StudentLectureViewHolder.class,
                        database.child("lectures").child(courseId)
                ) {
                    @Override
                    protected void populateViewHolder(StudentLectureViewHolder viewHolder, Lecture lecture, int position) {
                        viewHolder.populate(lecture);
                    }
                };
        quizRecyclerView.setAdapter(quizRecyclerAdapter);
        lectureRecyclerView.setAdapter(lectureRecyclerAdapter);
    }

    public static class StudentQuizViewHolder extends RecyclerView.ViewHolder {
        private static final LinearLayout.LayoutParams HEIGHT_ZERO = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        private static ViewGroup.LayoutParams defaultParams;
        private String quizId;
        private String quizName;
        private View view;

        public StudentQuizViewHolder(View view) {
            super(view);
            this.view = view;
            defaultParams = view.getLayoutParams();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Activity activity = (Activity) context;
                    Intent intent = new Intent(context, StudentQuizActivity.class);
                    intent.putExtra("courseId", courseId);
                    intent.putExtra("courseName", courseName);
                    intent.putExtra("courseInstructors", courseInstructors);
                    intent.putExtra("color", color);
                    intent.putExtra("quizId", quizId);
                    intent.putExtra("quizName", quizName);
                    context.startActivity(intent);
                }
            });
        }

        public void populate(Quiz quiz) {
            quizId = quiz.getId();
            quizName = quiz.getName();
            if (quiz.isActive() && !quiz.checkCompleted(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                view.setVisibility(View.VISIBLE);
                view.setLayoutParams(defaultParams);
                MaterialLetterIcon dateIcon = view.findViewById(R.id.gpRowIcon);
                TextView title = view.findViewById(R.id.gpTitle);
                dateIcon.setLetter("Quiz");
                dateIcon.setShapeColor(Color.argb(255, 175, 0, 0));
                title.setText(quizName);
            } else {
                view.setVisibility(View.GONE);
                view.setLayoutParams(HEIGHT_ZERO);
            }
        }
    }

    public static class StudentLectureViewHolder extends RecyclerView.ViewHolder {
        private String lectureId;
        private String lectureName;
        private View view;

        public StudentLectureViewHolder(View view) {
            super(view);
            this.view = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, StudentLectureActivity.class);
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
