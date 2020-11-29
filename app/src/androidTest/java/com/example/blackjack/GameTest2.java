package com.example.blackjack;

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
public class GameTest2 {
    String UID = "2tPIBsRrCxcC9Hh19gEmfu3kgdS2";
    double betAmount = 1;

    @Rule
    public ActivityTestRule<Game> mMainActivityTestRule = new ActivityTestRule<Game>(Game.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, Game.class);
            result.putExtra("USER", UID);
            result.putExtra("BET", betAmount);
            return result;
        }
    };

    @Test
    public void checkButtons(){ //test the use case of settings
        onView(withId(R.id.returnButton)).check(matches(not(isEnabled())));
        onView(withId(R.id.hitMeButton)).perform(click());
        onView(withId(R.id.userPile)).check(matches(withText(containsString("User Pile:"))));
        onView(withId(R.id.stopButton)).perform(click());
        onView(withId(R.id.hitMeButton)).check(matches(not(isEnabled())));
        onView(withId(R.id.stopButton)).check(matches(not(isEnabled())));
        onView(withId(R.id.returnButton)).check(matches(isEnabled()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkBetAmount(){ //test the use case of settings
        onView(withId(R.id.gameStatus)).check(matches(withText(containsString("1"))));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkDefeat(){ //test the use case of settings
        onView(withId(R.id.stopButton)).perform(click());
        onView(withId(R.id.gameStatus)).check(matches(withText(containsString("DEFEAT"))));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}