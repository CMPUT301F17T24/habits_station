/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by CBB on 2017/11/29.
 */

public class ViewEventActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public ViewEventActivityTest() {
        super(com.tiejun.habit_station.ViewEventActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testDelete() {
        solo.assertCurrentActivity("Wrong Activity", ViewEventActivity.class);
        solo.clickOnView(solo.getView(R.id.delete));
        solo.waitForText("Successfully deleted the event! ");
    }
}
