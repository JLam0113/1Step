package com.example.a1step;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private int tempSteps = 0;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button settings = findViewById(R.id.btnSetting);
        final Button leaderboard = findViewById(R.id.btnLeader);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        String temp = "users/" + auth.getCurrentUser().getUid() + "/totalSteps";
        DatabaseReference ref = db.getReference(temp);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(sensorEventListener, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalSteps = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                Intent intent = new Intent(MainActivity.this, LeaderBActivity.class);
                startActivity(intent);
            }
        });
        }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(tempSteps<1){
                tempSteps = (int) sensorEvent.values[0];
            }
            stepCounter = (int) sensorEvent.values[0] - tempSteps;
            totalSteps += stepCounter;
            TextView stepsText = findViewById(R.id.steps);
            stepsText.setText(stepCounter);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //Nothing to do here
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        auth.getInstance().signOut();
        // Update database with steps
    }
}