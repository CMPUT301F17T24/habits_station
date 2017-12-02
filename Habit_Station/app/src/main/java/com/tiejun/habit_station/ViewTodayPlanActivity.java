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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Activity to show today's plan
 *
 * @author xuanyi
 * @version 1.5
 * @see HabitEvent
 * @see HabitEventList
 * @since 1.0
 *
 */
public class ViewTodayPlanActivity extends AppCompatActivity {

    private ListView today;
    protected ArrayAdapter<Habit> adapter;
    private ArrayList<Habit> fillist  = new ArrayList<Habit>();
    private String habit_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_today_plan);
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");

        today = (ListView)findViewById(R.id.plan);
        habit_query = "{\n" +
                "  \"query\": { \n" +
                " \"term\" : { \"uName\" : \"" + userName + "\" }\n" +
                " 	}\n" +
                "}";
        ElasticSearchHabitController.GetHabits getHabits
                = new  ElasticSearchHabitController.GetHabits();
        getHabits.execute(habit_query);

        try {
            fillist.clear();
            fillist.addAll(getHabits.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        HabitList results =new  HabitList(fillist);
        ArrayList<Habit> fillist = results.getTodayHabits();
        Log.d("fill", String.valueOf(fillist.size()));

        adapter = new ArrayAdapter<Habit>(this, R.layout.list_habits, fillist);
        today.setAdapter(adapter);

    }
    
}
