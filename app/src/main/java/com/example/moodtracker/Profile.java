package com.example.moodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Profile extends AppCompatActivity {

    Button btnSaveProfile;
    Button btnCancelProfile;
    EditText name;
    EditText age;
    EditText weight;
    EditText height;
    RadioGroup radioGroup;
    RadioButton rbtnButton;
    ImageButton ibtnSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnCancelProfile = findViewById(R.id.btnCancelProfile);
        name = findViewById(R.id.etName);
        age = findViewById(R.id.etAge);
        weight = findViewById(R.id.etWeight);
        height = findViewById(R.id.etHeight);
        radioGroup = findViewById(R.id.radio);

        ibtnSettings = findViewById(R.id.ibtnSettings6);

        ibtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Settings.class);
                startActivity(i);
            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tallenna tiedot eka

                int radioId = radioGroup.getCheckedRadioButtonId();
                rbtnButton = findViewById(radioId);

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });


        btnCancelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.friends:
                        startActivity(new Intent(getApplicationContext(), Friends.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.chart:
                        startActivity(new Intent(getApplicationContext(), Chart.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                    case R.id.logout:
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });
    }

    public void checkRadio(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        rbtnButton = findViewById(radioId);
    }
}