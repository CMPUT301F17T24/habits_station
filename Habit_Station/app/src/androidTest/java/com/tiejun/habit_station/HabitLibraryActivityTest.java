/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

import com.tiejun.habit_station.AddHabitActivity;
import com.tiejun.habit_station.ElasticSearchUserController;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by CBB on 2017/11/7.
 */

public class HabitLibraryActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    private ListView habitList;
    protected HabitList habits = new HabitList();

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
        /*
        User user = new User(255, "testUser");
        ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
        addUserTask.execute(user);
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute("testName");
        try {
            user = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }
        try {
            habits = user.getHabitList();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the habits out of the async object");
        }
        */
        habitList = (ListView) solo.getView(R.id.habits);

        // if habit list is empty, add a new habit
        if (habitList.getCount() == 0) {
            solo.clickOnButton(R.id.add);
            solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);

            // identical to AddHabitActivityTest
            solo.enterText((EditText) solo.getView(R.id.title), "testHabit");
            solo.enterText((EditText) solo.getView(R.id.reason), "testReason");
            solo.clickOnButton(R.id.SAT);
            solo.clickOnButton(R.id.SUN);
            solo.clickOnButton(R.id.OK);
            solo.assertCurrentActivity("Wrong Activity", HabitLibraryActivity.class);
        }

        solo.clickLongInList(0);

    }

    public void testAddHabit() {
        solo.assertCurrentActivity("Wrong Activity", HabitLibraryActivity.class);
        solo.clickOnButton(R.id.add);
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
