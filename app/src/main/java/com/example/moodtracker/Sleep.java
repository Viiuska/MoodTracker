package com.example.moodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class Sleep extends AppCompatActivity {

    SmileRating smileRatingSleep;
    Button btnSaveSleep;
    Button btnCancelSleep;
    ImageButton ibtnSettings;
    EditText etSleepDiary;
    EditText etTimeSleep;
    TextView tvDateTimeSleep;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String person;
    String filename = ".Sleep.csv";
    String sleepLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        smileRatingSleep =  findViewById(R.id.smile_rating_sleep);
        btnSaveSleep = findViewById(R.id.btnSaveSleep);
        btnCancelSleep = findViewById(R.id.btnCancelSleep);
        etTimeSleep = findViewById(R.id.etTimeSleep);
        etSleepDiary = findViewById(R.id.etSleepDiary);
        tvDateTimeSleep = findViewById(R.id.tvDateTimeSleep);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        person = fAuth.getCurrentUser().getUid();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = simpleDateFormat.format(calendar.getTime());
        tvDateTimeSleep.setText(currentDate);



        ibtnSettings = findViewById(R.id.ibtnSettings8);

        ibtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Settings.class);
                startActivity(i);
            }
        });

        smileRatingSleep.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {
                switch (smiley) {
                    case SmileRating.TERRIBLE:
                        sleepLevel = "TERRIBLE";
                        break;
                    case SmileRating.BAD:
                        sleepLevel = "BAD";
                        break;
                    case SmileRating.OKAY:
                        sleepLevel = "OKAY";
                        break;
                    case SmileRating.GOOD:
                        sleepLevel = "GOOD";
                        break;
                    case SmileRating.GREAT:
                        sleepLevel = "GREAT";
                        break;
                    case SmileRating.NONE:
                        sleepLevel = "NONE";
                        break;
                }
            }
        });


        btnSaveSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile(filename);
                createFile(person);

                String udate = tvDateTimeSleep.getText().toString().trim();
                String uhour = etTimeSleep.getText().toString().trim();
                String udiary = etSleepDiary.getText().toString().trim();

                writeFile(udate, uhour, sleepLevel, udiary, person);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        btnCancelSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

    }

    public void createFile(String person){
        try {
            String content = "Date;Hour;Sleep;Text;\n";
            File file = new File(this.getFilesDir().getPath()+"/"+person+filename);

            if (!file.exists()) {
                file.createNewFile();
                OutputStreamWriter writer = new OutputStreamWriter(this.openFileOutput(person+filename, Context.MODE_PRIVATE));
                writer.write(content);
                writer.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void writeFile(String date, String time, String level, String text, String person){
        try (FileWriter fw = new FileWriter(this.getFilesDir().getPath()+"/"+person+filename, true)){
            BufferedWriter writer = new BufferedWriter(fw);
            writer.append(date+";"+time+";"+level+";"+text+"\n");
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public int readFile(String name, String person){
        int sleepTotal=0;
        try {
            Scanner inputStream = new Scanner(new File(this.getFilesDir().getPath()+"/"+person+name));
            String line = inputStream.nextLine();

            while (inputStream.hasNextLine()){
                line = inputStream.nextLine();
                String[] ary = line.split(";");
                int weeksSleep = Integer.parseInt(ary[1]);
                sleepTotal+= weeksSleep;
            }
            System.out.println("Moro, unen määrä "+ sleepTotal);
            return sleepTotal;

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return sleepTotal;
    }
}