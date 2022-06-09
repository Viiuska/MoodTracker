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
import com.hsalf.smilerating.SmileRating;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Mood extends AppCompatActivity {

    SmileRating smileRating;
    Button btnSaveMood;
    Button btnCancelMood;
    ImageButton ibtnSettings;
    EditText etMoodDiary;

    TextView tvDateTime;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String person;
    String filenameMood = ".Mood.csv";
    String filenameMoodOverall = ".MoodOverall.csv";
    String moodLevel;
    int moodNum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        smileRating =  findViewById(R.id.smile_rating_mood);
        btnSaveMood = findViewById(R.id.btnSave);
        btnCancelMood = findViewById(R.id.btnCancel);
        etMoodDiary = findViewById(R.id.etMoodDiary);

        tvDateTime = findViewById(R.id.tvDateTime);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        person = fAuth.getCurrentUser().getUid();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        SimpleDateFormat sDF = new SimpleDateFormat("dd.MM");

        String currentDate = simpleDateFormat.format(calendar.getTime());
        tvDateTime.setText(currentDate);
        String dayMonth = sDF.format(calendar.getTime());

        ibtnSettings = findViewById(R.id.ibtnSettings5);

        ibtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Settings.class);
                startActivity(i);
            }
        });

        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {
                switch (smiley) {
                    case SmileRating.TERRIBLE:
                        moodLevel = "TERRIBLE";
                        moodNum = 1;
                        break;
                    case SmileRating.BAD:
                        moodLevel = "BAD";
                        moodNum=2;
                        break;
                    case SmileRating.OKAY:
                        moodLevel = "OKAY";
                        moodNum = 3;
                        break;
                    case SmileRating.GOOD:
                        moodLevel = "GOOD";
                        moodNum = 4;
                        break;
                    case SmileRating.GREAT:
                        moodLevel = "GREAT";
                        moodNum = 5;
                        break;
                    case SmileRating.NONE:
                        moodLevel = "NONE";
                        moodNum = 0;
                        break;
                }
            }
        });

        btnSaveMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text ="Date;Mood;Text;\n";
                createFile(filenameMood, text, filenameMood);
                String textOverall = "Date;Mood;\n";
                createFile(filenameMoodOverall, textOverall, filenameMoodOverall);
                createFile(person, text,filenameMood);
                createFile(person, textOverall,filenameMoodOverall);

                String udate = tvDateTime.getText().toString().trim();
                String udiary = etMoodDiary.getText().toString().trim();

                writeFile(udate, moodLevel, udiary, person);
                writeFileOverall(dayMonth, moodNum, person, filenameMoodOverall);

                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        btnCancelMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });

    }

    public void createFile(String person, String content, String filename){
        try {
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


    public void writeFile(String date, String level, String text, String person){
        try (FileWriter fw = new FileWriter(this.getFilesDir().getPath()+"/"+person+filenameMood, true)){
            BufferedWriter writer = new BufferedWriter(fw);
            writer.append(date+";"+level+";"+text+"\n");
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void writeFileOverall(String date, int level, String person, String name){
        BufferedReader br = null;
        String line ="";
        String[] lines;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            br = new BufferedReader(new FileReader(this.getFilesDir().getPath() +"/"+person+filenameMoodOverall));
            while ((line = br.readLine()) != null) {
                line = line + ",";
                stringBuffer.append(line);
            }
            String result = stringBuffer.toString();
            lines = result.split(",");
            System.out.println("result "+result);

            String wanted = lines[lines.length-1];
            String[] moodOverallInfo = wanted.split(";");

                if(moodOverallInfo[0].equals(date)){
                    try (FileWriter fw = new FileWriter(this.getFilesDir().getPath()+"/"+person+filenameMoodOverall, false)){
                        BufferedWriter writer = new BufferedWriter(fw);
                        int daysMood = Integer.parseInt(moodOverallInfo[1]);
                        daysMood+=level;
                        int daysOverallMood= daysMood/2;
                        result = result.replaceAll(wanted, (date+";"+daysOverallMood+"\n"));
                        writer.write(result);
                        writer.flush();
                        writer.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                else{
                    try (FileWriter fw = new FileWriter(this.getFilesDir().getPath()+"/"+person+filenameMoodOverall, true)){
                        BufferedWriter writer = new BufferedWriter(fw);
                        writer.append(date+";"+level+"\n");
                        writer.flush();
                        writer.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }

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


}