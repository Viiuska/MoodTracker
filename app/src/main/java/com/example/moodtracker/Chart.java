package com.example.moodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Chart extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView tvNotes, tvHeadline;
    private Button btnClose;


    ArrayList moodArrayList;
    ArrayList sleepArrayList;

    BarChart moodChart;
    BarChart sleepChart;
    Button btnMoodNotes;
    Button btnSleepNotes;
    ImageButton ibtnSettings;

    String person;
    String filenameMood = ".Mood.csv";
    String filenameMoodOverall = ".MoodOverall.csv";
    String filenameSleep = ".Sleep.csv";

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        btnMoodNotes = findViewById(R.id.btnMoodNotes);
        btnSleepNotes = findViewById(R.id.btnSleepNotes);
        moodChart = findViewById(R.id.moodChart);
        sleepChart = findViewById(R.id.sleepChart);

        ibtnSettings = findViewById(R.id.ibtnSettings2);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        person = fAuth.getCurrentUser().getUid();

        ibtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Settings.class);
                startActivity(i);
            }
        });

        btnMoodNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headline="Your mood data";
                createNotesDialog(filenameMood, headline);
            }
        });

        btnSleepNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headline="Your sleep data";
                createNotesDialog(filenameSleep, headline);
            }
        });

        getMoodData(filenameMoodOverall);
        BarDataSet moodDataSet = new BarDataSet(moodArrayList,"1=Terrible, 2=Bad, 3=Okay, 4=Good and 5=Great");
        BarData moodBarData = new BarData(moodDataSet);
        moodChart.setData(moodBarData);
        moodDataSet.setColors(Color.GRAY);
        moodDataSet.setValueTextColor(Color.BLACK);
        moodDataSet.setValueTextSize(16f);
        moodChart.getDescription().setEnabled(true);

        getSleepData(filenameSleep);
        BarDataSet sleepDataSet = new BarDataSet(sleepArrayList,"Sleep in hours");
        BarData sleepBarData = new BarData(sleepDataSet);
        sleepChart.setData(sleepBarData);
        sleepDataSet.setColors(Color.GRAY);
        sleepDataSet.setValueTextColor(Color.BLACK);
        sleepDataSet.setValueTextSize(16f);
        sleepChart.getDescription().setEnabled(true);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.chart);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.friends:
                        startActivity(new Intent(getApplicationContext(), Friends.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.chart:
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

    public void createNotesDialog(String filename, String text){
        dialogBuilder = new AlertDialog.Builder(this);
        final View notesPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        tvHeadline = notesPopupView.findViewById(R.id.tvHeadline);
        tvNotes = notesPopupView.findViewById(R.id.tvNotes);
        btnClose = notesPopupView.findViewById(R.id.btnClose);

        dialogBuilder.setView(notesPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        tvHeadline.setText(text);

        readFile(person, filename);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void readFile(String person, String filename){
        BufferedReader br = null;
        String line ="";
        StringBuffer stringBuffer = new StringBuffer();
        try {
            br = new BufferedReader(new FileReader(this.getFilesDir().getPath() +"/"+person+filename));
            while ((line = br.readLine()) != null){
                stringBuffer.append(line+"\n");
            }
            tvNotes.setText(stringBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void getMoodData(String name){
        moodArrayList = new ArrayList();
        int row = 0;
        int vertaus=0;
        int vert=1;

        try {
            Scanner inputStream = new Scanner(new File(this.getFilesDir().getPath() + "/" + person + name));
            String line = inputStream.nextLine();
            while (inputStream.hasNextLine()) {
                line = inputStream.nextLine();
                row++;
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
            if (row - 6 <= vertaus) {
                while (row >= vertaus) {
                    row--;
                    String wanted = slines[vert];
                    vert++;
                    vertaus++;
                    String[] moodInfo = wanted.split(";");
                    int weeksMood = Integer.parseInt(moodInfo[1]);
                    moodArrayList.add(new BarEntry(vert,weeksMood));

                }
            } else {
                for (int lastRows = row - 6; lastRows <= row; lastRows++) {
                    String wanted = slines[lastRows];
                    String[] moodInfo = wanted.split(";");

                    int weeksMood = Integer.parseInt(moodInfo[1]);
                    moodArrayList.add(new BarEntry(lastRows,weeksMood));

                }

            }
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

    }




    private void getSleepData(String name){
        sleepArrayList = new ArrayList();
        int row = 0;
        int vertaus=0;
        int vert=1;

        try {
            Scanner inputStream = new Scanner(new File(this.getFilesDir().getPath() + "/" + person + name));
            String line = inputStream.nextLine();
            while (inputStream.hasNextLine()) {
                line = inputStream.nextLine();
                row++;
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
            if (row - 6 <= vertaus) {
                while (row >= vertaus) {
                    row--;
                    String wanted = slines[vert];
                    vert++;
                    vertaus++;
                    String[] sleepInfo = wanted.split(";");
                    int weeksSleep = Integer.parseInt(sleepInfo[1]);

                    sleepArrayList.add(new BarEntry(vert,weeksSleep));
                }
            } else {
                for (int lastRows = row - 6; lastRows <= row; lastRows++) {
                    String wanted = slines[lastRows];
                    String[] sleepInfo = wanted.split(";");

                    int weeksSleep = Integer.parseInt(sleepInfo[1]);
                    sleepArrayList.add(new BarEntry(lastRows,weeksSleep));
                }

            }
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

    }
}