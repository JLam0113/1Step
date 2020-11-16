package com.example.a1step;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserSettings userSettings);

    //THIS QUERY IS NOT CORRECT
    @Query("SELECT * FROM user_settings LIMIT 1")
    LiveData<UserSettings> getCurGoal();
}
