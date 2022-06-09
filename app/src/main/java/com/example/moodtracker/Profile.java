package com.example.moodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class Profile extends AppCompatActivity {

    Button btnSaveProfile;
    Button btnCancelProfile;
    EditText name;
    EditText age;
    EditText weight;
    EditText height;
    RadioGroup radioGroup;
    RadioButton rbtnButton;
    RadioButton btnF, btnM, btnO;
    ImageButton ibtnSettings;

    String person;
    String filename = ".ProfileInfo.csv";
    String sex;


    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


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
        btnF = findViewById(R.id.radio_female);
        btnM = findViewById(R.id.radio_male);
        btnO = findViewById(R.id.radio_other);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        person = fAuth.getCurrentUser().getUid();


        btnF.setChecked(update("Female"));
        btnM.setChecked(update("Male"));
        btnO.setChecked(update("Other"));

        String [] userInfo = readFile(filename, person);
        if(userInfo != null){
            name.setText(userInfo[0]);
            age.setText(userInfo[1]);

            btnF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean f_isChecked) {
                    saveSex("Female", f_isChecked);
                }
            });

            btnM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean m_isChecked) {
                    saveSex("Male", m_isChecked);
                }
            });

            btnO.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean o_isChecked) {
                    saveSex("Other", o_isChecked);
                }
            });

            weight.setText(userInfo[3]);
            height.setText(userInfo[4]);
        }else {
            name.setText("Name: ");
            age.setText("Age: ");
            weight.setText("Weight(kg): ");
            height.setText("Height(cm): ");
        }


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
                int radioId = radioGroup.getCheckedRadioButtonId();
                rbtnButton = findViewById(radioId);

                if(radioId == 2131296651){
                    sex = "Female";
                }
                if(radioId == 2131296652){
                    sex ="Male";
                }
                if(radioId == 2131296653) {
                    sex = "Other";
                }

                createFile(filename);
                createFile(person);

                String uname = name.getText().toString().trim();
                String uage = age.getText().toString().trim();
                String uweight= weight.getText().toString().trim();
                String uheight = height.getText().toString().trim();

                writeFile(uname, uage, sex, uweight, uheight, person);

                Toast.makeText(Profile.this, "User information saved", Toast.LENGTH_SHORT).show();
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


    public void saveSex(String key, boolean value){
        SharedPreferences sp = getSharedPreferences("Gender", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean update (String key){
        SharedPreferences sp = getSharedPreferences("Gender", MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public void createFile(String person){
        try {
            String content = "Name;Age;Sex;Weight;Height\n";
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

    public void writeFile(String name, String age, String sex, String weight, String height, String person){
        try (FileWriter fw = new FileWriter(this.getFilesDir().getPath()+"/"+person+filename, true)){
            BufferedWriter writer = new BufferedWriter(fw);
            writer.append(name+";"+age+";"+sex+";"+weight+";"+height+"\n");
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