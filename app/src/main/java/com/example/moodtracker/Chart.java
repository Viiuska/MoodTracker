package com.example.moodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Chart extends AppCompatActivity {


    ArrayList moodArrayList;
    ArrayList sleepArrayList;

    BarChart moodChart;
    BarChart sleepChart;
    Button btnMoodNotes;
    Button btnSleepNotes;
    ImageButton ibtnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        btnMoodNotes = findViewById(R.id.btnMoodNotes);
        btnSleepNotes = findViewById(R.id.btnSleepNotes);

        moodChart = findViewById(R.id.moodChart);
        sleepChart = findViewById(R.id.sleepChart);
        ibtnSettings = findViewById(R.id.ibtnSettings2);

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
                //avaa tiedoston johon kirjotettu mielitilaa....vaikka pop up lehdelle
            }
        });


        btnSleepNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //avaa tiedoston johon kirjotettu untatilaa....vaikka pop up lehdelle
            }
        });


        getMoodData();
        BarDataSet moodDataSet = new BarDataSet(moodArrayList,"10=Terrible, 20=Bad, 30=Okay, 40=Good and 50=Great");
        BarData moodBarData = new BarData(moodDataSet);
        moodChart.setData(moodBarData);
        moodDataSet.setColors(Color.GRAY);
        moodDataSet.setValueTextColor(Color.BLACK);
        moodDataSet.setValueTextSize(16f);
        moodChart.getDescription().setEnabled(true);

        getSleepData();
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

    private void getMoodData(){
        moodArrayList = new ArrayList();

        //Ota tiedostosta noi tiedot tuohon arraylistaan
        //eli päivämäärä ekaan ja tunne numero tokaan

        moodArrayList.add(new BarEntry(2f,10));
        moodArrayList.add(new BarEntry(3f,20));
        moodArrayList.add(new BarEntry(4f,30));
        moodArrayList.add(new BarEntry(5f,40));
        moodArrayList.add(new BarEntry(6f,50));
        moodArrayList.add(new BarEntry(7f,45));
        moodArrayList.add(new BarEntry(8f,10));

    }

    private void getSleepData(){
        sleepArrayList = new ArrayList();

        //Ota tiedostosta noi tiedot tuohon arraylistaan
        // eli päivämäärä ekaan ja tunnit jälkeiseen

        sleepArrayList.add(new BarEntry(2f,10));
        sleepArrayList.add(new BarEntry(3f,7));
        sleepArrayList.add(new BarEntry(4f,6));
        sleepArrayList.add(new BarEntry(5f,8));
        sleepArrayList.add(new BarEntry(6f,8));
        sleepArrayList.add(new BarEntry(7f,5));
        sleepArrayList.add(new BarEntry(8f,7));

    }
}