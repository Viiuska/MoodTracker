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
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    Button btnMoodAdd;
    Button btnSleepAdd;
    ScrollView svWebsites;
    Button btnMieli;
    Button btnSleep;
    Button btnAnxiety;
    Button btnMentally;
    ImageButton ibtnSettings;
    TextView tvSleepNum;
    TextView tvMoodState;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String person;
    String filenameMood = ".Mood.csv";
    String filenameSleep = ".Sleep.csv";

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    String currentDate = simpleDateFormat.format(calendar.getTime());

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
        tvSleepNum = findViewById(R.id.tvSleepNum);
        tvMoodState = findViewById(R.id.tvMoodState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        person = fAuth.getCurrentUser().getUid();

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

        int sleepInfo = readFileSleep(filenameSleep, person);
        tvSleepNum.setText(String.valueOf(sleepInfo+"h per night"));


        String [] moodInfo = readFileMood(filenameMood, person);
        if(moodInfo != null){
            tvMoodState.setText(moodInfo[1]);
        }

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

    public int readFileSleep(String name, String person) {
        int sleepTotal = 0;
        int row = 0;
        int div=0;
        String today = "";
        String sleepToday = "";

        try {
            Scanner inputStream = new Scanner(new File(this.getFilesDir().getPath() + "/" + person + name));
            String line = inputStream.nextLine();

            while (inputStream.hasNextLine()) {
                line = inputStream.nextLine();
                String[] ary = line.split(";");
                today = ary[0];
                sleepToday = ary[1];
                row++;
            }
            div=row;
            if (String.valueOf(currentDate).equals(today)) {
                btnSleepAdd.setText(sleepToday + " hour");
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //Let's calculate the weeks average sleep.
        BufferedReader br = null;
        try {
            String sline;
            String[] slines;
            br = new BufferedReader(new FileReader(this.getFilesDir().getPath() + "/" + person + name));
            StringBuffer buffer = new StringBuffer();
            while ((sline = br.readLine()) != null) {
                sline = sline + ",";
                buffer.append(sline);
            }
            String result = buffer.toString();
            slines = result.split(",");

            if (row - 6 < 0) {
                while (row > 0) {
                    String wanted = slines[row];
                    String[] sleepInfo = wanted.split(";");
                    row--;
                    int weeksSleep = Integer.parseInt(sleepInfo[1]);
                    sleepTotal += weeksSleep;
                }
                sleepTotal/=div;
            } else {
                for (int lastRows = row - 5; lastRows <= row; lastRows++) {
                    String wanted = slines[lastRows];
                    String[] sleepInfo = wanted.split(";");

                    int weeksSleep = Integer.parseInt(sleepInfo[1]);
                    sleepTotal += weeksSleep;
                }
                sleepTotal/=7;
            }

            return sleepTotal;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return sleepTotal;
    }

    public String [] readFileMood(String name, String person){
        BufferedReader br = null;
        try {
            String line;
            String[] lines;
            br = new BufferedReader(new FileReader(this.getFilesDir().getPath()+"/"+person+name));
            StringBuffer buffer = new StringBuffer();
            while((line = br.readLine())!= null){
                line = line+",";
                buffer.append(line);
            }
            String result = buffer.toString();
            lines = result.split(",");

            String wanted = lines[lines.length-1];
            String[] moodInfo = wanted.split(";");
            return moodInfo;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (br != null){
                    br.close();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
        String[] moodInfo =null;
        return  moodInfo;
    }
}