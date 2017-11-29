/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by CBB on 2017/11/7.
 */

public class AddHabitActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public AddHabitActivityTest() {
        super(com.tiejun.habit_station.AddHabitActivity.class);
    }

    /**
     * Runs at the beginning of the tests
     * @throws Exception
     */
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testAdd() {
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);

        solo.enterText((EditText) solo.getView(R.id.title), "test habit");
        solo.enterText((EditText) solo.getView(R.id.reason), "test reason");

        // testEmptyTitle()
        solo.clearEditText((EditText) solo.getView(R.id.title));
        solo.clickOnView(solo.getView(R.id.OK));
        solo.waitForText("Title should not be empty and should be at most 20 words");
        solo.enterText((EditText) solo.getView(R.id.title), "test habit");

        // testEmptyReason()
        solo.clearEditText((EditText) solo.getView(R.id.reason));
        solo.clickOnView(solo.getView(R.id.OK));
        solo.waitForText("Reason should not be empty and should be at most 30 words");
        solo.enterText((EditText) solo.getView(R.id.reason), "test reason");

        // testEmptyWeekday()
        solo.clickOnView(solo.getView(R.id.OK));
        solo.waitForText("The plan cannot be empty, select the days of a week.");
        solo.clickOnView(solo.getView(R.id.SAT));
        solo.clickOnView(solo.getView(R.id.SUN));

        // testAddSuccess()
        solo.clickOnView(solo.getView(R.id.OK));
        solo.waitForText("Successfully added a new habit! ");
    }

    /**
     * Runs at the end of the tests
     * @throws Exception
     */
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
