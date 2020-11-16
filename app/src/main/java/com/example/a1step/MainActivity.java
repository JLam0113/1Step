package com.example.a1step;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private SensorManager sensorManager;
    private int stepCounter = 0;
    private int totalSteps = 0;
    private double totalCals = 0;
    private int tempSteps = 0;
    private FirebaseDatabase db;
    private User user;
    private DatabaseReference ref;
    TextView tCalories;
    TextView tSteps;
    TextView stepsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button settings = findViewById(R.id.btnSetting);
        final Button leaderboard = findViewById(R.id.btnLeader);
        tCalories = findViewById(R.id.totalCals);
        tSteps = findViewById(R.id.totalSteps);
        stepsText = findViewById(R.id.steps);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        //String temp = "users/" + auth.getCurrentUser().getUid() + "/totalSteps";
        //String temp = "users/" + auth.getCurrentUser().getUid();
        ref = db.getReference("users/" + auth.getCurrentUser().getUid());
        Query query = db.getReference("users/" + auth.getCurrentUser().getUid());
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(sensorEventListener, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        query.addListenerForSingleValueEvent(valueEventListener);

        // Initialize nickname textView
        TextView nickname = findViewById(R.id.nicknameText);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setTotalSteps(totalSteps);
                totalCals += (totalSteps * 0.045);
                user.setTotalCalories(totalCals);
                ref.setValue(user);
                Intent intent = new Intent(MainActivity.this, LeaderBActivity.class);
                startActivity(intent);
            }
        });
        }

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                totalSteps = user.getTotalSteps();
                totalCals = user.getTotalCalories();
                tSteps.setText(String.valueOf(totalSteps));
                tCalories.setText(Double.toString(totalCals));
                stepsText.setText(String.valueOf(totalSteps));
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(tempSteps<1){
                tempSteps = (int) sensorEvent.values[0];
            }
            stepCounter = (int) sensorEvent.values[0] - tempSteps;
            totalSteps += stepCounter;
            stepsText.setText(stepCounter);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //Nothing to do here
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        //auth.getInstance().signOut();
        // Update database with steps
        user.setTotalSteps(totalSteps);
        totalCals += totalSteps * 0.045;
        user.setTotalCalories(totalCals);
        ref.setValue(user);
    }
}