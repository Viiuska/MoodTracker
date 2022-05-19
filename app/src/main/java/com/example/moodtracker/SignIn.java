package com.example.moodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    Button btnSigIn;
    ProgressBar progressBar;
    TextView tvOldUser;
    EditText etEmail2;
    EditText etPassword2;
    EditText etPassword3;
    FirebaseAuth fAuth;
    ImageButton ibtnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etEmail2= findViewById(R.id.etEmail2);
        etPassword2 = findViewById(R.id.etPassword3);
        etPassword3 = findViewById(R.id.etPassword3);
        fAuth = FirebaseAuth.getInstance();

        btnSigIn = findViewById(R.id.btnSignin);
        progressBar = findViewById(R.id.progressBar);
        tvOldUser = findViewById(R.id.tvOldUser);

        ibtnSettings = findViewById(R.id.ibtnSettings7);

        ibtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Settings.class);
                startActivity(i);
            }
        });

        if(fAuth.getCurrentUser()!=null){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }


        btnSigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail2.getText().toString().trim();
                String pass = etPassword2.getText().toString().trim();
                String pass2 = etPassword3.getText().toString().trim();
                String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{12,}";

                if(TextUtils.isEmpty(email)){
                    etEmail2.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    etPassword2.setError("Password is required");
                    return;
                }

                if(!pass.matches(pattern)) {
                    etPassword2.setError("Password must have at least: 12 character, one uppercase, one number and one symbol");
                    System.out.println(pass.matches(pattern));
                    return;
                }
                if(!pass.equals(pass2)){
                    etPassword3.setError("Passwords are not the same");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignIn.this,"User created", Toast.LENGTH_SHORT);
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        }else {
                            Toast.makeText(SignIn.this, "Error "+task.getException(), Toast.LENGTH_SHORT);
                        }
                    }
                });

            }
        });

        tvOldUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });


    }
}