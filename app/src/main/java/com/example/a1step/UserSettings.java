package com.example.a1step;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_settings")
public class UserSettings {

    @PrimaryKey
    @NonNull
    @ColumnInfo
    private String id;

    @ColumnInfo
    public int dailyGoal;

    @ColumnInfo
    public int dailySteps;

    @ColumnInfo
    public String date;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id){
        this.id = id;
    }

    public void setDailyGoal(@NonNull int dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

    public int getDailyGoal() {
        return dailyGoal;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public String getDate(){return date;}

    public int getDailySteps(){
        return dailySteps;
    }

    public void setDailySteps(@NonNull int dailySteps){
        this.dailySteps = dailySteps;
    }
}
