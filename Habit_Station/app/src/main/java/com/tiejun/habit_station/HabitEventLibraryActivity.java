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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class HabitEventLibraryActivity extends AppCompatActivity {

    private TextView title;
    private ListView events;
    protected HabitEventList habitEventList = new HabitEventList();
    protected ArrayAdapter<HabitEvent> adapter;
    private int click_item_index;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_library);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");
        Intent intent = getIntent();
        final int habitIndex = intent.getIntExtra("habit index", 0);

        //title = (TextView) findViewById(R.id.title);

        events = (ListView)findViewById(R.id.events);
        Button addButton = (Button) findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(HabitEventLibraryActivity.this, EditHabitEventActivity.class);
                intent.putExtra("habit index", habitIndex);

                intent.putExtra("select", 9);

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

        Intent intent = getIntent();
        final int habitIndex = intent.getIntExtra("habit index", 0);

        //Log.d("user_habL_start",userName);

        User user = new User();
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(userName);
        try {
            user = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }



        habitEventList = user.getHabitList().getHabit(habitIndex).getHabitEventList();
        adapter = new ArrayAdapter<HabitEvent>(this, R.layout.list_habits, habitEventList.sortEvents());
        events.setAdapter(adapter);

        title = (TextView) findViewById(R.id.title);
        String habit_name  = user.getHabitList().getHabit(habitIndex).getTitle();
        title.setText(habit_name);



    }





}
