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
import android.widget.ListView;

import com.robotium.solo.Solo;

/**
 * Created by CBB on 2017/11/11.
 */

public class HabitEventLibraryActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public HabitEventLibraryActivityTest() {
        super(com.tiejun.habit_station.HabitEventLibraryActivity.class);
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

    public void testViewEvent() {
        solo.assertCurrentActivity("Wrong Activity", HabitEventLibraryActivity.class);
        ListView habitEventList = (ListView) solo.getView(R.id.events);

        // if habit event list is empty, add a new habit event
        if (habitEventList.getCount() == 0) {
            solo.clickOnView(solo.getView(R.id.add));
            solo.assertCurrentActivity("Wrong Activity", EditHabitEventActivity.class);

            // identical to EditHabitEventActivityTest
            solo.enterText((EditText) solo.getView(R.id.comment), "test comment");
            solo.clickOnView(solo.getView(R.id.save));
            solo.assertCurrentActivity("Wrong Activity", HabitEventLibraryActivity.class);
        }
        solo.clickLongInList(0);
        solo.clickOnMenuItem("View Event details");
        solo.assertCurrentActivity("Wrong Activity", ViewEventActivity.class);
    }

    public void testEditEvent() {
        solo.assertCurrentActivity("Wrong Activity", HabitEventLibraryActivity.class);
        ListView habitEventList = (ListView) solo.getView(R.id.events);

        // if habit event list is empty, add a new habit event
        if (habitEventList.getCount() == 0) {
            solo.clickOnView(solo.getView(R.id.add));
            solo.assertCurrentActivity("Wrong Activity", EditHabitEventActivity.class);

            // identical to EditHabitEventActivityTest
            solo.enterText((EditText) solo.getView(R.id.comment), "test comment");
            solo.clickOnView(solo.getView(R.id.save));
            solo.assertCurrentActivity("Wrong Activity", HabitEventLibraryActivity.class);
        }
        solo.clickLongInList(0);
        solo.clickOnMenuItem("Edit Events");
        solo.assertCurrentActivity("Wrong Activity", EditHabitEventActivity.class);
    }

    public void testAddEvent() {
        solo.assertCurrentActivity("Wrong Activity", HabitEventLibraryActivity.class);
        solo.clickOnView(solo.getView(R.id.add));
        solo.assertCurrentActivity("Wrong Activity", EditHabitEventActivity.class);
    }

    public void testHome() {
        solo.assertCurrentActivity("Wrong Activity", HabitEventLibraryActivity.class);
        solo.clickOnView(solo.getView(R.id.home));
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
    }

    public void testBack() {
        solo.assertCurrentActivity("Wrong Activity", HabitEventLibraryActivity.class);
        solo.clickOnView(solo.getView(R.id.home));
        solo.assertCurrentActivity("Wrong Activity", HabitLibraryActivity.class);
    }

    /**
     * Runs at the end of the tests
     * @throws Exception
     */
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}