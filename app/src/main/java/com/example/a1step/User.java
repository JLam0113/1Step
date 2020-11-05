package com.example.a1step;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class User {
    private String email;
    double tSteps, tCalories;

    public User(String email, double tSteps, double tCalories) {
        this.email = email;
        this.tSteps = tSteps;
        this.tCalories = tCalories;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setTotalSteps(double tSteps){
        this.tSteps = tSteps;
    }

    public double getTotalSteps(){
        return tSteps;
    }

    public void setTotalCalories(double tCalories){
        this.tCalories = tCalories;
    }

    public double getTotalCalories(){
        return tCalories;
    }
}
