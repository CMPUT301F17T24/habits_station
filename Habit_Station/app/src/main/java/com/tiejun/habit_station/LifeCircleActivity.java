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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.round;

/**
 * Activity to show the life cycle of each habit event
 *
 * @author xuanyi
 * @version 1.0
 *
 */

public class LifeCircleActivity extends AppCompatActivity {
    private User user;
    private ListView Fhabits;
    private ArrayList<Habit> habits = new ArrayList<Habit>();
    private ArrayAdapter<Habit> adapter;

    private ArrayList<String> friendHabit = new ArrayList<String>();
    private ArrayAdapter<String> adapter1;


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


        for (Habit element: habits){
            int total = totalDays(element);
            int  complete = completeDays(element);

            String info = element.getuName()+" >> " +element.getTitle()+"\nprogress: " +complete +"/" + total;
            friendHabit.add(info);

        }

    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        adapter1 = new ArrayAdapter<String>(this, R.layout.list_habits, friendHabit);
        Fhabits.setAdapter(adapter1);
    }

    /**
     * Defining our own Comparator
     */
    class OrderByHabitType implements Comparator<Habit>
    {
        @Override
        public int compare(Habit h1, Habit h2)
        {
            return h1.getTitle().compareTo(h2.getTitle());
        }
    }

    /**
     * Calculate the complete dates of a habit
     * @param habit habit object
     * @return
     */
    protected int completeDays (Habit habit) {

        ArrayList<HabitEvent> fillist = new ArrayList<HabitEvent>();
        String event_query = "{\n" +
                "  \"query\": { \n" +
                "\"bool\": {\n" +
                "\"must\": [\n" +
                "{" + " \"term\" : { \"uName\" : \"" + habit.getuName() + "\" }},\n" +
                "{" + " \"match\" : {  \"eName\" : \"" + habit.getTitle() + "\" }}\n" +
                "]" +
                "}" +
                "}" +
                "}";

        ElasticSearchEventController.GetEvents getHEvent
                = new ElasticSearchEventController.GetEvents();
        getHEvent.execute(event_query);
        int num= 0;
        try {
           num = getHEvent.get().size();
            //fillist.addAll(getHEvent.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("complete",String.valueOf(fillist.size()));
        return num;

    }

    /**
     * Calculate the total days of a habit from its start date
     *
     * @param habit
     * @return
     */
    protected int totalDays (Habit habit) {
        Calendar start = habit.getStartDate();
        Calendar today = Calendar.getInstance();
        if(start.after(today)){
            return 0;
        }
        ArrayList<Integer> repeat = new ArrayList<>(habit.getRepeatWeekOfDay());//get repeat date
        int maxDays = 0;
        int total = 0;
        maxDays = 36500;

        for (int d = 1; d <= maxDays; d++) {
            if (start.after(today)) {
                break;
            }
            int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
            if (repeat.contains(dayOfWeek)) {
                total++;
            }
            start.add(Calendar.DATE, 1); //next day
        }

        return total;
    }











}
