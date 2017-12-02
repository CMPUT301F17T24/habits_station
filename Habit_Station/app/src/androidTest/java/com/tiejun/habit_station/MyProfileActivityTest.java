/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by CBB on 2017/11/29.
 */

public class MyProfileActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public MyProfileActivityTest() {
        super(com.tiejun.habit_station.MyProfileActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testGo() {
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);

        String userName = "test user";
        String followName = "test user";
        solo.clickOnView(solo.getView(R.id.go));
        if (followName.equals(userName)) {
            solo.waitForText("You cannot follow yourself");
        }

        followName = "test follow";
        solo.clickOnView(solo.getView(R.id.go));
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(followName);
        try {
            User user = getUserTask.get();
            if(user != null){
                ArrayList<String> pendingList = user.getPendingPermission();
                ArrayList<String> followerList = user.getFollower();

                if ( followerList.contains(userName)) {
                    solo.waitForText("You already followed this user");
                } else {
                    if (pendingList.contains(userName)) {
                        solo.waitForText("You are already in pending list");
                    } else {
                        solo.waitForText("You are successfully added to the pending list!");
                    }
                }
            } else {
                solo.waitForText("The user does not exist");
            }
        } catch (Exception e) {
            //
        }
    }

    public void testFriend() {
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);
        solo.clickOnView(solo.getView(R.id.friend));
        solo.assertCurrentActivity("Wrong Activity", FriendsActivity.class);
    }

    public void testLifeCircle() {
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);
        solo.clickOnView(solo.getView(R.id.lifyCycle));
        solo.assertCurrentActivity("Wrong Activity", LifeCircleActivity.class);
    }

    public void testNearby() {
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);
        solo.clickOnView(solo.getView(R.id.nearby));
        solo.assertCurrentActivity("Wrong Activity", NearbyActivity.class);
    }
    /*
    public void testRecommend() {
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);
        solo.clickOnView(solo.getView(R.id.recommand));
        solo.assertCurrentActivity("Wrong Activity", FriendsExploreActivity.class);
    }
    */
}