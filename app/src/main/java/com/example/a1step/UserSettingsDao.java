package com.example.a1step;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserSettings userSettings);

    @Update
    void updateUser(UserSettings userSettings);

    @Query("SELECT * FROM user_settings WHERE id LIKE :uid LIMIT 1")
    UserSettings findByUserID(String uid);
}
