/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
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

public class SignUpActivity extends SignInActivity {
    protected EditText username;
    private String userName;
    int uid;
    //public UserList userList = new UserList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

       username = (EditText) findViewById(R.id.username);

        Button signUp = (Button) findViewById(R.id.signup);
        signUp.setOnClickListener( new View.OnClickListener (){
            public void onClick(View v) {
                userName = username.getText().toString();
                //uid = userList.getSize();
                uid = 0;
                User user = new User(uid,userName);
                if (existedUser(userName)) {
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
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

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
