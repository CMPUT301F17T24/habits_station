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
 * Created by CBB on 2017/11/11.
 */

public class EditHabitEventActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public EditHabitEventActivityTest() {
        super(com.tiejun.habit_station.EditHabitEventActivity.class);
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

    // USELESS TEST SINCE NO HABIT IS PASSED AS ARGUMENT
    /*
    public void testEdit() {
        solo.assertCurrentActivity("Wrong Activity", EditHabitEventActivity.class);

        //test long comment
        solo.clearEditText((EditText) solo.getView(R.id.comment));
        solo.enterText((EditText) solo.getView(R.id.comment), "boo boo boo boo boo boo");
        solo.clickOnView(solo.getView(R.id.save));
        solo.waitForText("should be at most 20 words");

        //edit successful
        solo.clearEditText((EditText) solo.getView(R.id.comment));
        solo.enterText((EditText) solo.getView(R.id.comment), "test comment");
        solo.clickOnView(solo.getView(R.id.save));
        solo.waitForText("Successfully updated the event.");
    }
    */

    /**
     * Runs at the end of the tests
     * @throws Exception
     */
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}