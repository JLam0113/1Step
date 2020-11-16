package com.example.a1step;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_settings")
public class UserSettings {
    //Remove auto generate later

    @PrimaryKey
    @NonNull
    @ColumnInfo
    private String email;

    @ColumnInfo
    public String dailyGoal;

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getNickname() {
        return dailyGoal;
    }

    public void setNickname(String dailyGoal) {
        this.dailyGoal = dailyGoal;
    }
}
