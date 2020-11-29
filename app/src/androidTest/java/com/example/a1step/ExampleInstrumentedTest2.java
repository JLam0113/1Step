package com.example.a1step;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest2 {

    @Rule
    public ActivityScenarioRule<SettingActivity> settingRule = new ActivityScenarioRule<>(SettingActivity.class);

    @Test
    public void testNavigationBar(){
        onView(withId(R.id.leaderboardPage)).perform(ViewActions.click());
        Intents.init();
        onView(withId(R.id.textView12)).check(ViewAssertions.matches(withText("1Step Leaderboard")));
        Intents.release();
        onView(withId(R.id.settingsPage)).perform(ViewActions.click());
        Intents.init();
        onView(withId(R.id.textView)).check(ViewAssertions.matches(withText("Change Password")));
        Intents.release();
        onView(withId(R.id.homePage)).perform(ViewActions.click());
        Intents.init();
        onView(withId(R.id.textView5)).check(ViewAssertions.matches(withText("Total Steps Taken:")));
        Intents.release();
    }
}
