package com.example.a1step;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        final EditText password = findViewById(R.id.editCurrPass);
        final EditText newPass = findViewById(R.id.editNewPass);
        final EditText confPass = findViewById(R.id.editConfPass);
        final Button change = findViewById(R.id.btnChange);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

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
    }
}
