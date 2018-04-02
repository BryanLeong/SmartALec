package com.example.smartalec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginFieldsValid()) {
                    final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "", "Authentication in progress...");
                    auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, CourseListActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().isEmpty()) {
                    auth.sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this,
                                                "Password reset email sent", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this,
                                                "Error: User does not exist!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Error: Email field is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean loginFieldsValid() {
        boolean emailEmpty = email.getText().toString().isEmpty();
        boolean passwordEmpty = password.getText().toString().isEmpty();
        if (emailEmpty && passwordEmpty) {
            Toast.makeText(LoginActivity.this, "Error: Email and password fields are empty!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (emailEmpty) {
            Toast.makeText(LoginActivity.this, "Error: Email field is empty!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (passwordEmpty) {
            Toast.makeText(LoginActivity.this, "Error: Password field is empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
