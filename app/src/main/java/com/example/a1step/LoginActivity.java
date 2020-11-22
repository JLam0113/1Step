package com.example.a1step;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Date;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText email = findViewById(R.id.editEmail);
        final EditText password = findViewById(R.id.editPass);
        final Button login = findViewById(R.id.btnLogin);
        final Button register = findViewById(R.id.btnRegister);
        auth = FirebaseAuth.getInstance();

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email2 = email.getText().toString();
                final String password2 = password.getText().toString();
                if (TextUtils.isEmpty(email2)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password2)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //authenticate user
                auth.signInWithEmailAndPassword(email2, password2)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password2.length() < 6) {
                                        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Authentication failed, incorrect email or password!", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    UserSettings userSettings = UserSettingsRoomDB.getDatabase(getApplicationContext()).userSettingsDao().findByUserID(auth.getCurrentUser().getUid());
                                    if(userSettings == null){
                                        userSettings = new UserSettings();
                                        userSettings.setDailySteps(0);
                                        userSettings.setDate(new Date(System.currentTimeMillis()).toString());
                                        userSettings.setDailyGoal(0);
                                        userSettings.setNotification(false);
                                        userSettings.setId(auth.getCurrentUser().getUid());
                                        UserSettingsRoomDB.getDatabase(getApplicationContext()).userSettingsDao().insert(userSettings);
                                    }
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }
}
