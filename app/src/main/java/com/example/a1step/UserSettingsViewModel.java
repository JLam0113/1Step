package com.example.a1step;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UserSettingsViewModel extends AndroidViewModel {
    private UserSettingsRepo mRepository;
    private final LiveData<UserSettings> mGoal;
    public UserSettingsViewModel(Application application) {
        super(application);
        mRepository = new UserSettingsRepo(application);
        mGoal = mRepository.getCurGoal();
    }

    LiveData<UserSettings> getCurGoal() { return mGoal; }

    public void insert(UserSettings userSettings) { mRepository.insert(userSettings); }
}