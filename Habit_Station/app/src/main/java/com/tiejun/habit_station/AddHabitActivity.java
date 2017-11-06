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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashSet;

public class AddHabitActivity extends AppCompatActivity {
    private EditText title;
    //private EditText date;
    private DatePicker simpleDatePicker;
    private EditText reason;
    public  int set_year = 0,
            set_month = 0,
            set_day = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        final String userName = pref.getString("currentUser", "");

        title = (EditText) findViewById(R.id.title);
        reason = (EditText) findViewById(R.id.reason);
        simpleDatePicker = (DatePicker)findViewById(R.id.datePicker);

        final HashSet<Integer> weekDay = new HashSet<Integer>();


        final Button MBtn = (Button) findViewById(R.id.M);                        //  click the button Monday
        MBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                weekDay.add(1);
            }
        });

        final Button TBtn = (Button) findViewById(R.id.T);                        //  click the button Tuesday
        TBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                weekDay.add(2);
            }
        });

        final Button WBtn = (Button) findViewById(R.id.W);                        //  click the button Wednesday
        WBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                weekDay.add(3);
            }
        });


        final Button RBtn = (Button) findViewById(R.id.R);                        //  click the button Thursday
        RBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                weekDay.add(4);
            }
        });

        final Button FBtn = (Button) findViewById(R.id.F);                        //  click the button Friday
        FBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                weekDay.add(5);
            }
        });


        final Button SATBtn = (Button) findViewById(R.id.SAT);                        //  click the button Saturday
        SATBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                weekDay.add(6);
            }
        });

        final Button SUNBtn = (Button) findViewById(R.id.SUN);                        //  click the button Sunday
        SUNBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                weekDay.add(7);
            }
        });
 //////////////////////////////////////////////////////////////////////////



        final Button confirmBtn = (Button) findViewById(R.id.OK);                        //  click the button to save the information
        confirmBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                String sTitle = title.getText().toString();
                //String sDate = date.getText().toString();
                String sReason = reason.getText().toString();

                Calendar startDate = Calendar.getInstance();
                if (set_year == 0 && set_month == 0 && set_day == 0){
                    //
                    set_year = startDate.get(Calendar.YEAR);
                    set_month = startDate.get(Calendar.MONTH)+1;
                    set_day = startDate.get(Calendar.DAY_OF_MONTH);
                }
                startDate.set(set_year, set_month, set_day);

                if ((sTitle.length() == 0) || sTitle.length() > 20){
                    title.setError("Title should not be empty and should be at most 20 words");
                }


                if (sReason.length() > 30){
                    reason.setError("Reason should be at most 30 words");
                }


               boolean added = setHabit(userName , sTitle, sReason, startDate, weekDay);


                //Log.e("Add_user",userName);
                if (added) {
                    Intent intent = new Intent(AddHabitActivity.this, HabitLibraryActivity.class);
                    startActivity(intent);
                }

            }
        });

//////////////////////////////////////////////
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        simpleDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        set_year = simpleDatePicker.getYear();
                        set_month = simpleDatePicker.getMonth()+1;
                        set_day =  simpleDatePicker.getDayOfMonth();

                    }
                });

    }


    public boolean setHabit(String current_user,String sTitle,String sReason,Calendar startDate,HashSet<Integer> weekDay)
    {
        User user = new User();
        //String query = current_user;
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(current_user);
        try {
            user = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }


        //Log.d("CCC",habit.getTitle());

        Habit habit1 = new Habit(sTitle,sReason,startDate,weekDay);

        HabitEventList events = new HabitEventList();
        habit1.setHabitEventList(events);

        HabitList list = user.getHabitList();
        Log.d("CC",String.valueOf(list.getCount()));



        if (list.check_dup(habit1)){
            Toast.makeText(this, "This habit already exists!!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {

            list.add(habit1);

            HabitList list2 = new HabitList(list.getHabits());
            user.setHabitList(list2);


            Log.d("CCC", String.valueOf(list.getCount()));

            ElasticSearchUserController.AddUserTask addUserTask
                    = new ElasticSearchUserController.AddUserTask();
            addUserTask.execute(user);
            return true;
        }

    }








}
