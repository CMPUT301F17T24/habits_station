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
 * Created by CBB on 2017/11/27.
 */

public class ViewHabitActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public ViewHabitActivityTest() {
        super(com.tiejun.habit_station.ViewHabitActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testEdit() {
        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);
        solo.clickOnView(solo.getView(R.id.edit));
        solo.assertCurrentActivity("Wrong Activity", EditHabitActivity.class);
    }

    public void testStatus() {
        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);
        solo.clickOnView(solo.getView(R.id.edit));
        solo.assertCurrentActivity("Wrong Activity", StatusActivity.class);
    }
}
