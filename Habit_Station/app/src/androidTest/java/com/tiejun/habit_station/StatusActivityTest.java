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

public class StatusActivityTest extends ActivityInstrumentationTestCase2{
    private Solo solo;

    public StatusActivityTest() {
        super(com.tiejun.habit_station.StatusActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testReturn() {
        solo.assertCurrentActivity("Wrong Activity", StatusActivity.class);
        solo.clickOnView(solo.getView(R.id.cool));
        solo.assertCurrentActivity("Wrong Activity", HabitLibraryActivity.class);
    }
}
