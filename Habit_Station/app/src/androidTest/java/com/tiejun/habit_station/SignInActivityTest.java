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

        // create a new user
        User user = new User(255, "testUser");
        ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
        addUserTask.execute(user);
        ElasticSearchUserController.IsExist isExist = new ElasticSearchUserController.IsExist();
        isExist.execute("testUser");

        // attempt to log in
        solo.enterText((EditText) solo.getView(R.id.username), "testUser");
        solo.clickOnButton(R.id.signin);
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
    }

    public void testSignUp() {
        // test if the sign up button directs to sign up page
        solo.assertCurrentActivity("Wrong Activity", SignInActivity.class);
        solo.clickOnButton(R.id.signup);
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
