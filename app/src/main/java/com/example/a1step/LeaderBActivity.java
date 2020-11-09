package com.example.a1step;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LeaderBActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);
        final TextView leader = findViewById(R.id.leader);
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference node = db.getReference();
    }




}
