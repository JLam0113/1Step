package com.example.a1step;

import android.app.Application;

import androidx.lifecycle.LiveData;

class UserSettingsRepo {
    private UserSettingsDao mUserSettingsDao;
    private LiveData<UserSettings> mCurGoal;

    UserSettingsRepo(Application application) {
        UserSettingsRoomDB db = UserSettingsRoomDB.getDatabase(application);
        mUserSettingsDao = db.userSettingsDao();
        mCurGoal = mUserSettingsDao.getCurGoal();
    }

    LiveData<UserSettings> getCurGoal() {
        return mCurGoal;
    }

    void insert(UserSettings userSettings) {
        UserSettingsRoomDB.databaseWriteExecutor.execute(() -> {
            mUserSettingsDao.insert(userSettings);
        });
    }
}
