package com.example.smartalec;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.Random;

public class CourseListActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference database;
    private RecyclerView recyclerView;
    private volatile static User user;
    private MaterialLetterIcon coursesBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);
        database.child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        recyclerView = findViewById(R.id.courseRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        coursesBar = findViewById(R.id.coursesBar);
        coursesBar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                auth.signOut();
                Toast.makeText(CourseListActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CourseListActivity.this, LoginActivity.class));
                finish();
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Course, CourseViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Course, CourseViewHolder>(
                        Course.class,
                        R.layout.course_row,
                        CourseViewHolder.class,
                        database.child("courses")
                ) {
                    @Override
                    protected void populateViewHolder(CourseViewHolder viewHolder, Course course, int position) {
                        viewHolder.populate(course);
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        private static final LinearLayout.LayoutParams HEIGHT_ZERO = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        private static ViewGroup.LayoutParams defaultParams;
        private String courseId;
        private String courseName;
        private String courseInstructors;
        private int color;
        private View view;

        public CourseViewHolder(View view) {
            super(view);
            this.view = view;
            defaultParams = view.getLayoutParams();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent;
                    if (user.getType().equals("student")) {
                        intent = new Intent(context, InstructorCourseActivity.class);
                    } else {
                        intent = new Intent(context, StudentCourseActivity.class);
                    }
                    intent.putExtra("courseId", courseId);
                    intent.putExtra("courseName", courseName);
                    intent.putExtra("courseInstructors", courseInstructors);
                    intent.putExtra("color", color);
                    context.startActivity(intent);
                }
            });
        }

        public void populate(Course course) {
            if (user.getCourses().contains(course.getId())) {
                view.setVisibility(View.VISIBLE);
                view.setLayoutParams(defaultParams);
                MaterialLetterIcon courseIcon = view.findViewById(R.id.courseIcon);
                TextView courseNameTextView = view.findViewById(R.id.courseName);
                TextView courseInstructorsTextView = view.findViewById(R.id.courseInstructors);

                courseId = course.getId();
                courseName = course.getName();
                courseInstructors = course.getInstructors();
                color = getRandomColor();
                courseIcon.setLetter(courseId.substring(0, 2) + "." + courseId.substring(2));
                courseIcon.setShapeColor(color);
                courseNameTextView.setText(courseName);
                courseInstructorsTextView.setText(courseInstructors);
            } else {
                view.setVisibility(View.GONE);
                view.setLayoutParams(CourseViewHolder.HEIGHT_ZERO);
            }
        }

        private int getRandomColor() {
            Random rand = new Random();
            return Color.argb(255, rand.nextInt(175), rand.nextInt(175), rand.nextInt(175));
        }
    }
}
