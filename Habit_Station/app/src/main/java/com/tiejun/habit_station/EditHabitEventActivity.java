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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class EditHabitEventActivity extends AppCompatActivity {

    private TextView info;
    private EditText comment;
    private DatePicker simpleDatePicker;
    private CheckBox image,location;

    protected HabitEventList habitEventList = new HabitEventList();
    protected HabitEvent habitEvent;


    public  int do_year = 0,
            do_month = 0,
            do_day = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit_event);
    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");

        Intent intent = getIntent();
        final int habitIndex = intent.getIntExtra("habit index", 0);


        User user = new User();
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(userName);
        try {
            user = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }



        habitEventList = user.getHabitList().getHabit(habitIndex).getHabitEventList();


        info = (TextView) findViewById(R.id.info);
        Habit habit = user.getHabitList().getHabit(habitIndex);
        //String habit_name  = habit.getTitle();
        info.setText(habit.toString() +"\n"+habit.getReason()+"\n"+habit.getRepeatWeekOfDay());
        Log.d("TTT", String.valueOf(intent.getIntExtra("select",0)));



    }









}
