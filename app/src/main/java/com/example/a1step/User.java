package com.example.a1step;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class User {
    private String email;
    double tCalories;
    int tSteps;

    public User(String email, int tSteps, double tCalories) {
        this.email = email;
        this.tSteps = tSteps;
        this.tCalories = tCalories;
    }
    public User(){

    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setTotalSteps(int tSteps){
        this.tSteps = tSteps;
    }

    public int getTotalSteps(){
        return tSteps;
    }

    public void setTotalCalories(double tCalories){
        this.tCalories = tCalories;
    }

    public double getTotalCalories(){
        return tCalories;
    }
}
