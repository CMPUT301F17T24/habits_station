/*
 * Copyright (c) 2017 Team24, CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Activity for exploring friends
 *
 * @author xuanyi
 * @version 1.0
 *
 */


public class FriendsExploreActivity extends AppCompatActivity {

    private EditText search;
    private ListView recommend;
    protected ImageView go;

    private ArrayList<Habit> habitsList = new ArrayList<Habit>();
    private ArrayList<String> users = new ArrayList<String>();
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_explore);
        search = (EditText)findViewById(R.id.keyword);
        recommend = (ListView)findViewById(R.id.users);
        go = (ImageView)findViewById(R.id.go);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                String userName = pref.getString("currentUser", "");

                String keyword = search.getText().toString();
                String searchHabits = "{\n" +
                        "  \"query\": { \n" +
                        " \"wildcard\" : { \"title\" : \"" +"*"+ keyword + "*" +"\"  }\n"+
                        " 	}\n" +
                        "}";


                ElasticSearchHabitController.GetHabits getHabits
                        = new  ElasticSearchHabitController.GetHabits();
                getHabits.execute(searchHabits);

                try {
                    habitsList.clear();
                    habitsList.addAll(getHabits.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                users.clear();
                int len = habitsList.size();
                for (int i = 0; i<len; i++) {
                    Habit habit = habitsList.get(i);
                    if (! habit.getuName().equals(userName)){
                        String userInfo = habit.getuName() + "\nlikes " + habit.getTitle();
                        users.add(userInfo);
                    }
                }
                onStart();
            }
        });

    }



    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        adapter = new ArrayAdapter<String>(this, R.layout.list_habits, users);
        recommend.setAdapter(adapter);


    }







}
