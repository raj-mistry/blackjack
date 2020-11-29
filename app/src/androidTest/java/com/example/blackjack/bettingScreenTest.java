package com.example.blackjack;

import static org.junit.Assert.*;

import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.ViewAssertion;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class bettingScreenTest {

    String UID = "2tPIBsRrCxcC9Hh19gEmfu3kgdS2";

    @Rule
    public ActivityTestRule<bettingScreen> mMainActivityTestRule = new ActivityTestRule<bettingScreen>(bettingScreen.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, bettingScreen.class);
            result.putExtra("USER", UID);
            return result;
        }
    };

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.blackjack", appContext.getPackageName());
    }

    @Test
    public void testBet(){ //test the use case of settings
        onView(withId(R.id.betSlider)).perform(click());
        onView(withId(R.id.placeBet)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSwitchActivity() {
        onView(withId(R.id.betSlider)).perform(click());
        onView(withId(R.id.placeBet)).perform(click());
        onView(withId(R.id.gameStatus)).check(matches(withText(containsString("Current bet:"))));
    }
}
