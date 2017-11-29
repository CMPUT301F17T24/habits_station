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
import com.tiejun.habit_station.ElasticSearchUserController;

/**
 * Created by CBB on 2017/11/7.
 */

public class SignUpActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public SignUpActivityTest() {
        super(com.tiejun.habit_station.SignUpActivity.class);
    }

    /**
     * Runs at the beginning of the tests
     * @throws Exception
     */
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testSignUp() {
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);

        // testEmptyUsername()
        solo.clickOnView(solo.getView(R.id.signup));
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);

        // create a new user
        User user = new User(255, "test user");
        ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
        addUserTask.execute(user);
        solo.enterText((EditText) solo.getView(R.id.username), "test user");
        solo.clickOnView(solo.getView(R.id.signup));

        ElasticSearchUserController.IsExist isExist = new ElasticSearchUserController.IsExist();
        isExist.execute("test user");
        try {
            boolean Exist = isExist.get();
            assertTrue(Exist);
        } catch (Exception e){
            Log.i("Error", "Failed to get the User out of the async object");
        }
    }
}
