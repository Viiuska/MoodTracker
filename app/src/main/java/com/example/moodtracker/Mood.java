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
    String filename = ".Mood.csv";
    String moodLevel;

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String currentDate = simpleDateFormat.format(calendar.getTime());
        tvDateTime.setText(currentDate);


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
                        break;
                    case SmileRating.BAD:
                        moodLevel = "BAD";
                        break;
                    case SmileRating.OKAY:
                        moodLevel = "OKAY";
                        break;
                    case SmileRating.GOOD:
                        moodLevel = "GOOD";
                        break;
                    case SmileRating.GREAT:
                        moodLevel = "GREAT";
                        break;
                    case SmileRating.NONE:
                        moodLevel = "NONE";
                        break;
                }
            }
        });

        btnSaveMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile(filename);
                createFile(person);

                String udate = tvDateTime.getText().toString().trim();
                String udiary = etMoodDiary.getText().toString().trim();

                writeFile(udate, moodLevel, udiary, person);

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

    public void createFile(String person){
        try {
            String content = "Date;Mood;Text;\n";
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
        try (FileWriter fw = new FileWriter(this.getFilesDir().getPath()+"/"+person+filename, true)){
            BufferedWriter writer = new BufferedWriter(fw);
            writer.append(date+";"+level+";"+text+"\n");
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String [] readFile(String name, String person){
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
            String[] userInfo = wanted.split(";");
            return userInfo;
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
        String[] userInfo =null;
        return  userInfo;
    }
}