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
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainPageActivity extends SignInActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
//
//        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
//        final String userName = pref.getString("currentUser", "");
//        Log.d("username_main", userName);

        Button Button1 = (Button) findViewById(R.id.profile);
        Button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainPageActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });

        Button Button2 = (Button) findViewById(R.id.today);
        Button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainPageActivity.this, ViewTodayPlanActivity.class);
                startActivity(intent);
            }
        });


        Button Button3 = (Button) findViewById(R.id.habits);
        Button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainPageActivity.this, HabitLibraryActivity.class);
                startActivity(intent);
            }
        });

        Button Button4 = (Button) findViewById(R.id.history);
        Button4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainPageActivity.this, HabitHistoryActivity.class);
                startActivity(intent);
            }
        });


    }
}
