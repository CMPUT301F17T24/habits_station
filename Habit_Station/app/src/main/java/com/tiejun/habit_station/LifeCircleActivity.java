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
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

public class LifeCircleActivity extends AppCompatActivity {
    private User user;
    private ListView Fhabits;
    private ArrayList<Habit> habits = new ArrayList<Habit>();
    private ArrayAdapter<Habit> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_circle);
        Fhabits = (ListView)findViewById(R.id.fhabits);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(userName);
        try{
            user = getUserTask.get();
        }
        catch (Exception e) {
            Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
        }

        ArrayList<String> friends = user.getFollowee();     // get the users followed by the current user
        Collections.sort(friends);
        for (String element: friends){
            String query = "{\n" +
                    "  \"query\": { \n" +
                    " \"term\" : { \"uName\" : \"" + element + "\" }\n" +
                    " 	}\n" +
                    "}";
            ElasticSearchHabitController.GetHabits getHabits
                    = new  ElasticSearchHabitController.GetHabits();
            getHabits.execute(query);
            try {
                ArrayList<Habit> result = getHabits.get();
                Collections.sort(result, new OrderByHabitType() );
                habits.addAll(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }



    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        adapter = new ArrayAdapter<Habit>(this, R.layout.list_habits, habits);
        Fhabits.setAdapter(adapter);
    }

//Defining our own Comparator

    class OrderByHabitType implements Comparator<Habit>
    {
        @Override
        public int compare(Habit h1, Habit h2)
        {
            return h1.getTitle().compareTo(h2.getTitle());
        }
    }


}
