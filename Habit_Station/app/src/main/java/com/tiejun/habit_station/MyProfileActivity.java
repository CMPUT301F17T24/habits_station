/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity {

    private EditText search;
    private TextView profile;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        search = (EditText)findViewById(R.id.search);


        ImageView go_tab = (ImageView) findViewById(R.id.go);
        go_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String followName = search.getText().toString();
                addFollow(followName);
            }
        });


        Button friendButton = (Button) findViewById(R.id.friend);
        friendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MyProfileActivity.this, FriendsActivity .class);
                startActivity(intent);

            }
        });




    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");

        profile = (TextView)findViewById(R.id.profile);
        profile.setText("Welcome, "+userName);


    }









    private void addFollow(String followName) {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");

        if (followName.equals(userName)) {
            Toast.makeText(this, "You cannot follow yourself", Toast.LENGTH_SHORT).show();
        }
        else {
            ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
            getUserTask.execute(followName);
            try {
                User user = getUserTask.get();
                if(user != null){
                    ArrayList<String> pendingList = user.getPendingPermission();
                    ArrayList<String> followerList = user.getFollower();

                    if ( followerList.contains(userName)) {
                        Toast.makeText(this, "You already followed this user", Toast.LENGTH_SHORT).show();
                    } else {
                        if (pendingList.contains(userName)) {
                            Toast.makeText(this, "You are already in pending list", Toast.LENGTH_SHORT).show();
                        } else {
                            pendingList.add(userName);
                            user.setPendingPermissions(pendingList);
                            ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
                            addUserTask.execute(user);
                            Toast.makeText(this, "You are successfully added to the pending list!", Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {
                    Toast.makeText(this, "The user does not exist", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                //Log.i("Error", "Failed to get the User out of the async object");
            }
        }

    }






}
