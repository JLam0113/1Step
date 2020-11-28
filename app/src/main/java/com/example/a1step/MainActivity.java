package com.example.a1step;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Date;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private UserSettings userSettings;
    private FirebaseAuth auth;
    private SensorManager sensorManager;
    private int stepCounter = 0;
    private int totalSteps = 0;
    private double totalCals = 0;
    private int dailySteps = 0;
    private int tempSteps = 0;
    private int dailyGoal = 0;
    private FirebaseDatabase db;
    private User user;
    private DatabaseReference ref;
    private boolean n;
    TextView tCalories;
    TextView tSteps;
    TextView stepsText;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tCalories = findViewById(R.id.totalCals);
        tSteps = findViewById(R.id.totalSteps);
        stepsText = findViewById(R.id.steps);
        final TextView goal = findViewById(R.id.goal);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("users/" + auth.getCurrentUser().getUid());
        Query query = db.getReference("users/" + auth.getCurrentUser().getUid());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(sensorEventListener, stepSensor, SensorManager.SENSOR_DELAY_UI);
        query.addListenerForSingleValueEvent(valueEventListener);

        email = findViewById(R.id.emailText);
        userSettings = UserSettingsRoomDB.getDatabase(getApplicationContext()).userSettingsDao().findByUserID(auth.getCurrentUser().getUid());
        dailyGoal = userSettings.getDailyGoal();
        goal.setText(String.valueOf(userSettings.getDailyGoal()));
        dailySteps = userSettings.getDailySteps();
        stepsText.setText(String.valueOf(dailySteps));

        notification(userSettings);

        Date date = new Date(System.currentTimeMillis());
        if(!userSettings.getDate().equals(date.toString())){
            userSettings.setDate(date.toString());
            userSettings.setDailySteps(0);
        }

        //For bottom navigation menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.homePage);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.settingsPage:
                        updateUser();
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.homePage:
                        return true;
                    case R.id.leaderboardPage:
                        updateUser();
                        startActivity(new Intent(getApplicationContext(), LeaderBActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        }

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                totalSteps = user.getTotalSteps();
                totalCals = user.getTotalCalories();
                email.setText(user.getEmail());
                tSteps.setText(String.valueOf(totalSteps));
                DecimalFormat currency= new DecimalFormat("###,###.##");
                tCalories.setText(currency.format(totalCals));
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(tempSteps<1){
                tempSteps = (int) sensorEvent.values[0];
            }
            stepCounter = (int) sensorEvent.values[0] - tempSteps;
            stepCounter = stepCounter/3;
            totalSteps += stepCounter;
            dailySteps += stepCounter;
            totalCals += stepCounter * 0.045;
            stepsText.setText(String.valueOf(dailySteps));
            //Update totals
            tSteps.setText(String.valueOf(totalSteps));
            DecimalFormat currency= new DecimalFormat("###,###.##");
            tCalories.setText(currency.format(totalCals));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //Nothing to do here
        }
    };

    public void updateUser(){
        user.setTotalSteps(totalSteps);
        user.setTotalCalories(totalCals);
        ref.setValue(user);
        userSettings.setDailySteps(dailySteps);
        userSettings.setDailyGoal(dailyGoal);
        userSettings.setNotification(n);
        UserSettingsRoomDB.getDatabase(getApplicationContext()).userSettingsDao().updateUser(userSettings);
    }

    protected void onDestroy() {
        super.onDestroy();
        // Update database with steps
        updateUser();
    }

    public void notification(UserSettings userSettings){
        if(userSettings.getNotification()) {
            n = true;
            //UserSettingsRoomDB.getDatabase(getApplicationContext()).userSettingsDao().updateUser(userSettings);
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = notificationManager.getNotificationChannel("M_CH_ID");
                if (mChannel == null) {
                    mChannel = new NotificationChannel("M_CH_ID", "Daily Goal", importance);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notificationManager.createNotificationChannel(mChannel);
                }
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getBaseContext(), "M_CH_ID");

            notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.sneaker)
                    .setContentTitle("Daily Goal")
                    .setContentText("Progress: " + userSettings.getDailySteps() + "/" + userSettings.getDailyGoal())
                    .setContentInfo("Info");

            Notification notif = notificationBuilder.build();
            notif.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
            notificationManager.notify(1, notif);
        }
        else n = false;

    }
}