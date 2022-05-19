package com.example.moodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Friends extends AppCompatActivity {

    ImageButton ibtnSettings;
    Button btnFriend1;
    Button btnFriend2;
    Button btnFriend3;
    Button btnFriend4;
    Button btnFriend5;
    Button btnFriend6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        btnFriend1 = findViewById(R.id.btnFriend1);
        btnFriend2 = findViewById(R.id.btnFriend2);
        btnFriend3 = findViewById(R.id.btnFriend3);
        btnFriend4 = findViewById(R.id.btnFriend4);
        btnFriend5 = findViewById(R.id.btnFriend5);
        btnFriend6 = findViewById(R.id.btnFriend6);
        ibtnSettings = findViewById(R.id.ibtnSettings);

        btnFriend1.setText(" Allison");
        btnFriend2.setText(" Tim");
        btnFriend3.setText(" Matti");
        btnFriend4.setText(" Ida");
        btnFriend5.setText(" Tommi");
        btnFriend6.setText(" Steve");

        ibtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Settings.class);
                startActivity(i);
            }
        });




        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.friends);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.friends:
                        return true;
                    case R.id.chart:
                        startActivity(new Intent(getApplicationContext(), Chart.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
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

}