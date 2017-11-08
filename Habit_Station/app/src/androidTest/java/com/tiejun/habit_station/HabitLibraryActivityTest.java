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

import java.util.Calendar;
import java.util.HashSet;

import com.tiejun.habit_station.AddHabitActivity;

/**
 * Created by CBB on 2017/11/7.
 */

public class HabitLibraryActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public int set_year = 0;
    public int set_month = 0;
    public int set_day = 0;

    public HabitLibraryActivityTest() {
        super(com.tiejun.habit_station.HabitLibraryActivity.class);
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

    public void testViewHabit() {
        solo.assertCurrentActivity("Wrong Activity", HabitLibraryActivity.class);
        ListView listview = (ListView) solo.getView(R.id.habits);

        // if habit list is empty, add a new habit
        if (listview.getCount() == 0) {
            solo.clickOnButton("Add");
            solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);

            // identical to AddHabitActivityTest
            solo.enterText((EditText) solo.getView(R.id.title), "testHabit");
            solo.enterText((EditText) solo.getView(R.id.reason), "testReason");
            solo.clickOnButton("SAT");
            solo.clickOnButton("SUN");
            solo.clickOnButton("OK");
            solo.assertCurrentActivity("Wrong Activity", HabitLibraryActivity.class);
        }

        solo.clickLongInList(0);
    }

    public void testAddHabit() {
        solo.assertCurrentActivity("Wrong Activity", HabitLibraryActivity.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);
    }

    /**
     * Runs at the end of the tests
     * @throws Exception
     */
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
