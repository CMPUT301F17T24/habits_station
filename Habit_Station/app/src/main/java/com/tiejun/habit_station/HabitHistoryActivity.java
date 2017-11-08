/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class HabitHistoryActivity extends AppCompatActivity {

    private ListView history;
    protected HabitEventList histories = new HabitEventList();
    protected ArrayAdapter<HabitEvent> adapter;
    protected ImageView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");

        history = (ListView)findViewById(R.id.history);



    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");

        User user = new User();
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(userName);
        try {
            user = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }

        histories = user.getHistory();
        adapter = new ArrayAdapter<HabitEvent>(this, R.layout.list_habits, histories.sortEvents());
        history.setAdapter(adapter);



    }


}