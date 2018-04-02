package com.example.smartalec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        /*
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("1002172").build();
        auth.getCurrentUser().updateProfile(profileUpdates);
        */

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, CourseListActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
