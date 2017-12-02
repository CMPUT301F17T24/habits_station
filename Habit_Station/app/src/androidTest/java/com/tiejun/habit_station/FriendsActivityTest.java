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
import android.widget.ListView;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by CBB on 2017/12/2.
 */

public class FriendsActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    private User user;
    private ArrayList<String> uNames  = new ArrayList<String>();

    public FriendsActivityTest() { super(com.tiejun.habit_station.FriendsActivity.class); }

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

    public void testPending() {
        solo.assertCurrentActivity("Wrong Activity", FriendsActivity.class);

        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute("test user");
        try{
            user = getUserTask.get();
        }
        catch (Exception e) {
            Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
        }

        // test followee list
        solo.clickOnView(solo.getView(R.id.friends));

        // test pending list
        solo.clickOnView(solo.getView(R.id.pending));

        if (uNames.size() > 0) {
            solo.clickInList(0);
            solo.clickOnMenuItem("Accept");
            solo.waitForText("You accepted this request!");

            if (uNames.size() > 0) {
                solo.clickInList(0);
                solo.clickOnMenuItem("Ignore");
                solo.waitForText("You ignored this user! ");
            }
        }
    }

    /**
     * Runs at the end of the tests
     * @throws Exception
     */
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
