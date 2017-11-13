/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

/**
 * Created by CBB on 2017/11/11.
 */

public class HabitHistoryActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    private ListView history;

    public HabitHistoryActivityTest() {
        super(com.tiejun.habit_station.HabitHistoryActivity.class);
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

    // CANNOT TEST SINCE HABIT EVENT LIST IS NULL
    /*
    public void testSearch() {
        solo.assertCurrentActivity("Wrong Activity", HabitHistoryActivity.class);

        //test no search type
        solo.enterText((EditText) solo.getView(R.id.keyword), "test search");
        boolean check1 = solo.isCheckBoxChecked("Comment");
        if (check1)
            solo.clickOnView(solo.getView(R.id.comment));
        solo.clickOnView(solo.getView(R.id.search));
        solo.waitForText("Please select one");

        //test both search type checked
        boolean check2 = solo.isCheckBoxChecked("Type");
        if (!check1)
            solo.clickOnView(solo.getView(R.id.comment));
        if (!check2)
            solo.clickOnView(solo.getView(R.id.type));
        solo.clickOnView(solo.getView(R.id.search));
        solo.waitForText("Cannot select both");
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
