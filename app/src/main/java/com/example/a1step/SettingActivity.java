package com.example.a1step;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private UserSettings userSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        final EditText password = findViewById(R.id.editCurrPass);
        final EditText newPass = findViewById(R.id.editNewPass);
        final EditText confPass = findViewById(R.id.editConfPass);
        final EditText dailyGoal = findViewById(R.id.dailyGoal);
        final Button change = findViewById(R.id.btnChange);
        final Button delete = findViewById(R.id.btnDelete);
        final Button setGoal = findViewById(R.id.btnDaily);
        final SwitchCompat notification = findViewById(R.id.notification);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        userSettings = UserSettingsRoomDB.getDatabase(getApplicationContext()).userSettingsDao().findByUserID(auth.getCurrentUser().getUid());

        if(userSettings.getNotification()){
            notification.setChecked(true);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.delete();
                Toast.makeText(SettingActivity.this, "Your account has successfully been deleted.", Toast.LENGTH_SHORT).show();
                final DatabaseReference userNode = db.getReference(auth.getCurrentUser().getUid());
                userNode.removeValue();
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password2 = password.getText().toString().trim();
                String newPass2 = newPass.getText().toString().trim();
                String confPass2 = confPass.getText().toString().trim();
                if(TextUtils.isEmpty(password2)){
                    Toast.makeText(getApplicationContext(), "Enter current password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(newPass2)){
                    Toast.makeText(getApplicationContext(), "Enter new password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newPass2.length()<6){
                    Toast.makeText(getApplicationContext(), "New password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtils.equals(newPass2, confPass2)){
                    Toast.makeText(getApplicationContext(), "Passwords not equal!", Toast.LENGTH_SHORT).show();
                    return;
                }
                user.updatePassword(newPass2).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingActivity.this, "Password is updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SettingActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        setGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int dailyGoal2 = Integer.parseInt(dailyGoal.getText().toString().trim());
                    userSettings.setDailyGoal(dailyGoal2);
                    UserSettingsRoomDB.getDatabase(getApplicationContext()).userSettingsDao().updateUser(userSettings);
                    Toast.makeText(SettingActivity.this, "Your daily step goal has been updated.", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(SettingActivity.this, "Please enter a daily step goal.", Toast.LENGTH_SHORT).show();
                }
                if(userSettings.getNotification()) {
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
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                if (notification.isChecked()){
                    userSettings.setNotification(true);
                    UserSettingsRoomDB.getDatabase(getApplicationContext()).userSettingsDao().updateUser(userSettings);
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
                            .setContentText("Progress: " + userSettings.getDailySteps() + "/"+userSettings.getDailyGoal())
                            .setContentInfo("Info");

                    Notification notif = notificationBuilder.build();
                    notif.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
                    notificationManager.notify(1, notif);
                }
                else {
                    userSettings.setNotification(false);
                    UserSettingsRoomDB.getDatabase(getApplicationContext()).userSettingsDao().updateUser(userSettings);
                    notificationManager.cancelAll();
                }

            }});


        //For bottom navigation menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.settingsPage);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.settingsPage:
                        return true;
                    case R.id.homePage:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.leaderboardPage:
                        startActivity(new Intent(getApplicationContext(), LeaderBActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}
