package com.example.a1step;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest3 {
    @Rule
    public ActivityTestRule<LoginActivity> loginRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testFailedLogin() {
        LoginActivity activity = loginRule.getActivity();
        onView(withId(R.id.editEmail)).perform(ViewActions.typeText("test@gmail.com"));
        onView(withId(R.id.editPass)).perform(ViewActions.typeText("12345"));
        onView(withId(R.id.btnLogin)).perform(ViewActions.click());
        onView(withId(R.id.textView3)).check(ViewAssertions.matches(withText("Footstep Tracker")));
    }
}
