package com.example.a1step;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private UserSettingsDao dao;
    private UserSettingsRoomDB db;

    @Test
    public void testUser(){
        User user = new User();
        user.setTotalSteps(100);
        user.setTotalCalories(10);
        user.setEmail("test@email.com");

        assertEquals(user.getEmail(), "test@email.com");
        assertEquals(user.getTotalCalories(), 10, 0);
        assertEquals(user.getTotalSteps(), 100);
    }

    @Test
    public void testUserSettings(){
        UserSettings userSettings = new UserSettings();
        userSettings.setDailyGoal(1000);
        userSettings.setNotification(false);
        userSettings.setDailySteps(10);

        assertEquals(userSettings.getDailyGoal(),1000);
        assertEquals(userSettings.getDailySteps(),10);
        assertEquals(userSettings.getNotification(), false);
    }

}