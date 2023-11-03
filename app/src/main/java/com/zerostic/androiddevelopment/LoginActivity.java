package com.zerostic.androiddevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    MaterialCardView login;
    FirebaseAuth auth;
    LinearLayout signUp;
    TextView forgotPassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);
        forgotPassword = findViewById(R.id.forgotPassword);
        progressDialog = new ProgressDialog(this);
        login.setOnClickListener(v -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)) {
                Toast.makeText(LoginActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("Logging in...");
                progressDialog.show();
                loginUser(emailText, passwordText);
            }
        });
        signUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        forgotPassword.setOnClickListener(v -> {
            String emailText = email.getText().toString();
            if (TextUtils.isEmpty(emailText)){
                Toast.makeText(LoginActivity.this, "Please enter your email to change your password.", Toast.LENGTH_SHORT).show();
            }else {
                progressDialog.setMessage("Sending password reset email...");
                progressDialog.show();
                auth.sendPasswordResetEmail(emailText).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Password reset email sent successfully.", Toast.LENGTH_SHORT).show();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loginUser(String emailText, String passwordText) {
        auth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }else {
                Toast.makeText(LoginActivity.this, "Login Failed "+task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}