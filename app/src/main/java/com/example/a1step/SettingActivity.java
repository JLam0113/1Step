package com.example.a1step;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private DatabaseReference ref;
    private UserSettings userSettings;
    private int dailySteps;
    private int totalSteps;
    private int totalCals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        final EditText password = findViewById(R.id.editCurrPass);
        final EditText newPass = findViewById(R.id.editNewPass);
        final EditText confPass = findViewById(R.id.editConfPass);
        final Button change = findViewById(R.id.btnChange);
        final Button delete = findViewById(R.id.btnDelete);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        ref = db.getReference("users/" + auth.getCurrentUser().getUid());
        userSettings = UserSettingsRoomDB.getDatabase(getApplicationContext()).userSettingsDao().findByUserID(auth.getCurrentUser().getUid());

        delete.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          user.delete();
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
