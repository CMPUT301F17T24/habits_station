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

import com.robotium.solo.Solo;

/**
 * Created by CBB on 2017/11/7.
 */

public class SignInActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public SignInActivityTest() {
        super(com.tiejun.habit_station.SignInActivity.class);
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

    public void testSignIn() {
        solo.assertCurrentActivity("Wrong Activity", SignInActivity.class);

        // create a new user "testUser"
        User user = new User(255, "test user");
        ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
        addUserTask.execute(user);

        // testEmptyUsername()
        solo.clickOnView(solo.getView(R.id.signin));
        solo.assertCurrentActivity("Wrong Activity", SignInActivity.class);

        // attempt to log in with an unregistered account
        boolean Exist = false;
        ElasticSearchUserController.IsExist isExist1 = new ElasticSearchUserController.IsExist();
        isExist1.execute("guest");
        try {
            Exist = isExist1.get();
        } catch (Exception e){
            Log.i("Error", "Failed to get the User out of the async object");
        }
        solo.enterText((EditText) solo.getView(R.id.username), "guest");
        solo.clickOnView(solo.getView(R.id.signin));
        if (Exist){
            solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        } else {
            solo.assertCurrentActivity("Wrong Activity", SignInActivity.class);
        }

        // attempt to log in with a registered account
        ElasticSearchUserController.IsExist isExist2 = new ElasticSearchUserController.IsExist();
        isExist2.execute("test user");
        try {
            Exist = isExist2.get();
        } catch (Exception e){
            Log.i("Error", "Failed to get the User out of the async object");
        }
        solo.clearEditText((EditText) solo.getView(R.id.username));
        solo.enterText((EditText) solo.getView(R.id.username), "test user");
        solo.clickOnView(solo.getView(R.id.signin));
        if (Exist){
            solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        } else {
            solo.assertCurrentActivity("Wrong Activity", SignInActivity.class);
        }
    }

    public void testSignUp() {
        // test if the sign up button directs to sign up page
        solo.assertCurrentActivity("Wrong Activity", SignInActivity.class);
        solo.clickOnView(solo.getView(R.id.signup));
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
    }

    /**
     * Runs at the end of the tests
     * @throws Exception
     */
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
