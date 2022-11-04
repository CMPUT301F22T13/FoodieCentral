package com.example.cmput301f22t13.uilayer.userlogin;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class userAuthentication {

    @Rule
    public ActivityScenarioRule<Login> mActivityScenarioRule =
            new ActivityScenarioRule<>(Login.class);

    @Test
    public void userAuthentication() {
    }
}
