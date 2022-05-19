package com.example.moodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hsalf.smilerating.SmileRating;

public class Sleep extends AppCompatActivity {

    SmileRating smileRating;
    Button btnSaveSleep;
    Button btnCancelSleep;
    ImageButton ibtnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        smileRating =  findViewById(R.id.smile_rating_mood);
        btnSaveSleep = findViewById(R.id.btnSaveSleep);
        btnCancelSleep = findViewById(R.id.btnCancelSleep);

        ibtnSettings = findViewById(R.id.ibtnSettings8);

        ibtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Settings.class);
                startActivity(i);
            }
        });

        btnSaveSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        /*
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {
                switch (smiley) {
                    case SmileRating.BAD:
                        break;
                    case SmileRating.GOOD:
                        break;
                    case SmileRating.GREAT:
                        break;
                    case SmileRating.OKAY:
                        break;
                    case SmileRating.TERRIBLE:
                        break;
                    case SmileRating.NONE:
                        break;
                }
            }
        });

        smileRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
            }
        });

         */
    }
}