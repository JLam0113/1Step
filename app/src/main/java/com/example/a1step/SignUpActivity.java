package com.example.a1step;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText email = findViewById(R.id.editEmail);
        final EditText password = findViewById(R.id.editPass);
        Button register = findViewById(R.id.btnRegister);
        auth = FirebaseAuth.getInstance();
        final FirebaseDatabase db = FirebaseDatabase.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String email2 = email.getText().toString().trim();
                    String password2 = password.getText().toString().trim();
                    if (TextUtils.isEmpty(email2)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password2)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password2.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //create user
                    auth.createUserWithEmailAndPassword(email2, password2)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        User user = new User(email2, 0,0);
                                        final DatabaseReference userNode = db.getReference("users/" + auth.getCurrentUser().getUid());
                                        UserSettings userSettings = new UserSettings();
                                        userSettings.setDailyGoal(0);
                                        userSettings.setDailySteps(0);
                                        userSettings.setNotification(false);
                                        userSettings.setDate(new Date(System.currentTimeMillis()).toString());
                                        userSettings.setId(auth.getCurrentUser().getUid());
                                        UserSettingsRoomDB.getDatabase(getApplicationContext()).userSettingsDao().insert(userSettings);
                                        userNode.setValue(user);
                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }
                            });
                }
            });
        }
    }