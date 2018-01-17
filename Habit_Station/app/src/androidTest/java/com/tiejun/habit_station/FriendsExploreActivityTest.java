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
 * Created by CBB on 2017/12/2.
 */

public class FriendsExploreActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public FriendsExploreActivityTest() { super(com.tiejun.habit_station.FriendsExploreActivity.class); }

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

    public void testGo() {
        solo.assertCurrentActivity("Wrong Activity", FriendsExploreActivity.class);
        solo.enterText((EditText) solo.getView(R.id.keyword), "test habit");
        solo.clickOnView(solo.getView(R.id.go));
    }

    /**
     * Runs at the end of the tests
     * @throws Exception
     */
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
