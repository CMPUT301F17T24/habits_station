/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by CBB on 2017/11/7.
 */

public class MainPageActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public MainPageActivityTest() {
        super(com.tiejun.habit_station.MainPageActivity.class);
    }

    /**
     * Runs at the beginning of the tests
     * @throws Exception
     */
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testMyProfileActivity() {
        // test if the button directs to profile page
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.clickOnButton(R.id.profile);
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);
    }

    public void testViewTodayPlanActivity() {
        // test if the button directs to today's plan page
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.clickOnButton(R.id.today);
        solo.assertCurrentActivity("Wrong Activity", ViewTodayPlanActivity.class);
    }

    public void testHabitLibraryActivity() {
        // test if the button directs to habit library page
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.clickOnButton(R.id.library);
        solo.assertCurrentActivity("Wrong Activity", HabitLibraryActivity.class);
    }

    public void testHabitHistoryActivity() {
        // test if the button directs to habit history page
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.clickOnButton(R.id.history);
        solo.assertCurrentActivity("Wrong Activity", HabitHistoryActivity.class);
    }

    /**
     * Runs at the end of the tests
     * @throws Exception
     */
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
