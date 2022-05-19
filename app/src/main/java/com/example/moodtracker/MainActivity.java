package com.example.moodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button btnMoodAdd;
    Button btnSleepAdd;
    ScrollView svWebsites;
    Button btnMieli;
    Button btnSleep;
    Button btnAnxiety;
    Button btnMentally;
    ImageButton ibtnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMoodAdd = findViewById(R.id.btnMoodAdd);
        btnSleepAdd = findViewById(R.id.btnSleepAdd);
        svWebsites = findViewById(R.id.svWebsites);
        btnMieli = findViewById(R.id.btnFriend1);
        btnSleep = findViewById(R.id.btnSleep);
        btnAnxiety = findViewById(R.id.btnAnxiety);
        btnMentally = findViewById(R.id.btnMentally);

        btnMoodAdd.setOnClickListener(this::onClick);
        btnSleepAdd.setOnClickListener(this::onClick);
        btnMieli.setOnClickListener(this::onClick);
        btnSleep.setOnClickListener(this::onClick);
        btnAnxiety.setOnClickListener(this::onClick);
        btnMentally.setOnClickListener(this::onClick);

        ibtnSettings = findViewById(R.id.ibtnSettings4);

        ibtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Settings.class);
                startActivity(i);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

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
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        overridePendingTransition(0,0);
                        finish();
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnMoodAdd:
                Intent i = new Intent(getApplicationContext(), Mood.class);
                startActivity(i);
                break;
            case R.id.btnSleepAdd:
                Intent in = new Intent(getApplicationContext(), Sleep.class);
                startActivity(in);
                break;
            case R.id.btnFriend1:
                String Mieli ="https://mieli.fi/en/support-and-help/";
                Uri webAddress = Uri.parse(Mieli);
                Intent goMieli = new Intent(Intent.ACTION_VIEW, webAddress);
                if(goMieli.resolveActivity(getPackageManager()) == null){
                    startActivity(goMieli);
                }
                break;
            case R.id.btnSleep:
                String Sleep ="https://www.mentalhealth.org.uk/publications/how-sleep-better";
                Uri webAdd = Uri.parse(Sleep);
                Intent goSleep = new Intent(Intent.ACTION_VIEW, webAdd);
                if(goSleep.resolveActivity(getPackageManager()) == null){
                    startActivity(goSleep);
                }
                break;
            case R.id.btnAnxiety:
                String Anxiety ="https://psychcentral.com/anxiety/how-to-reduce-anxiety-quickly";
                Uri web = Uri.parse(Anxiety);
                Intent goAnxiety = new Intent(Intent.ACTION_VIEW, web);
                if(goAnxiety.resolveActivity(getPackageManager()) == null){
                    startActivity(goAnxiety);
                }
                break;
            case R.id.btnMentally:
                String Mentally ="https://www.mentalhealth.org.uk/publications/how-to-mental-health";
                Uri webMen = Uri.parse(Mentally);
                Intent goMentally = new Intent(Intent.ACTION_VIEW, webMen);
                if(goMentally.resolveActivity(getPackageManager()) == null){
                    startActivity(goMentally);
                }
                break;
        }
    }
}