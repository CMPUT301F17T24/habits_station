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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class HabitLibraryActivity extends SignInActivity {

    private ListView habitList;

    protected HabitList habits =new HabitList();
    protected ArrayAdapter<Habit> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_library);


        habitList = (ListView)findViewById(R.id.habits);
        Button addButton = (Button) findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);

                Intent intent = new Intent(HabitLibraryActivity.this, AddHabitActivity.class);
                startActivity(intent);

            }
        });

    }




    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();


        //Log.e("AAA", habits.get(0).toString());

        //loadFromFile();


        adapter = new ArrayAdapter<Habit>(this, R.layout.list_habits, habits.getHabits());
        habitList.setAdapter(adapter);


    }



}
