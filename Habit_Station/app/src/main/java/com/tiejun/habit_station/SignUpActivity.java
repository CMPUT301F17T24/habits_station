/*
 * Copyright (c) 2017 Team24, CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Sign up
 *
 * @author xtie
 * @version 1.0
 *
 */
public class SignUpActivity extends SignInActivity {
    protected EditText username;
    private String userName;
    int uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

       username = (EditText) findViewById(R.id.username);

        Button signUp = (Button) findViewById(R.id.signup);
        signUp.setOnClickListener( new View.OnClickListener (){
            public void onClick(View v) {
                userName = username.getText().toString();
                uid = userList.getCount();
                boolean sign = true;
                if (username.length() == 0){
                    username.setError("User name cannot be empty");
                    sign = false;
                }

                User user = new User(uid,userName);

                if (! userName.equals(userName.toLowerCase())){
                    username.setError("NO Capital letter for user name. ");
                }

                else if (existedUser(userName)) {
                    try{
                        Toast.makeText(getApplicationContext(),"Duplicate user name!",Toast.LENGTH_LONG).show();
                        throw new IllegalArgumentException("Duplicate user");
                    }catch(IllegalArgumentException e){

                    }
                }

                else {
                    userList.addUser(user);
                    ElasticSearchUserController.AddUserTask addUserTask
                        = new ElasticSearchUserController.AddUserTask();
                    addUserTask.execute(user);


                    if (sign) {
                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    /**
     * Check if the user is already existed
     * @param name username
     * @return
     */
    private boolean existedUser (String name) {
        ElasticSearchUserController.IsExist isExist = new ElasticSearchUserController.IsExist();
        isExist.execute(name);

        try {
            if (isExist.get()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
