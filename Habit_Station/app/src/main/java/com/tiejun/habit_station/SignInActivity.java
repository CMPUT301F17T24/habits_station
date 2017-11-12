/*
 * Copyright (c) 2017 TeamX, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact contact@abc.ca.
 */

package com.tiejun.habit_station;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {
    public UserList userList;
    public String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        userList = new UserList();

        //TextView testView = (TextView) findViewById(R.id.username);
        Button signIn = (Button) findViewById(R.id.signin);
        Button signUp = (Button) findViewById(R.id.signup);
        //userName = testView.getText().toString();
//        Log.d("username", userName);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView testView = (TextView) findViewById(R.id.username);
                userName = testView.getText().toString();
                //Log.d("username", userName);

                if (! userName.equals(userName.toLowerCase())){
                    testView.setError("NO Capital letter for user name. ");
                }
                else if (existedUser(userName)) {
                    Intent intent = new Intent(SignInActivity.this, MainPageActivity.class);
                    Log.d("username", userName);
                    storePreference(userName);
                    startActivity(intent);
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
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
                Toast.makeText(getApplicationContext(), name + " does not exist.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * This method stored the successfully login user's name in a local file
     * File explorer -> data -> data -> com.example.mac.bugfree -> sharef_prefs -> data.xml
     * @param name String of user name to be stored
     */
    private void storePreference(String name){
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("currentUser",name);
        editor.apply();
    }



}
