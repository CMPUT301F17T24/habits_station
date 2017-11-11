/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.lang3.ObjectUtils;
import org.w3c.dom.Text;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.tiejun.habit_station.R.id.date;
import static com.tiejun.habit_station.R.id.habits;
import static java.sql.Types.NULL;

/**
 *
 * this activity shows details about a habit
 * with button to start new intent to edit or show the status
 * fulfilled by using start for result
 */
public class ViewHabitActivity extends AppCompatActivity {

    protected HabitList habitsList =new HabitList();
    private TextView theTitle;
    private TextView theName;
    private TextView theReason;
    private TextView theDate;
    private TextView showRepeat;
    private Habit habit;
    private int index;
    private User user = new User();


    /**
     * online resource from stack overflow of pass variable back to main activity
     *
     * modified to start a new intent of edit habit
     *
     * requestcode 1 for edit and delete
     * if delete go back to the library
     *  if edit, update the content
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // edit or delete habit
            if(resultCode == RESULT_OK) {
                // set variable of edited data here
                int delSig =data.getIntExtra("delSig",0); //get signal of delete
                if( delSig == 1 ){
                    //delete the habit, tested and passed
                    habitsList.delete(habitsList.getHabit(index));
                    ElasticSearchUserController.AddUserTask addUserTask
                            = new ElasticSearchUserController.AddUserTask();
                    addUserTask.execute(user);
                    Intent deleteBackIntent = new Intent(getApplicationContext(), HabitLibraryActivity.class);
                    startActivity(deleteBackIntent);
                }
                if( delSig == 0){// edit
                    try{
                        habit.setReason(data.getStringExtra("newReason"));
                        habit.setTitle(data.getStringExtra("newTitle"));
                        String newDate = data.getStringExtra("newDate");
                        if( newDate != ""){
                            try {
                                String pattern = "yyyyMMdd";
                                Date date = new SimpleDateFormat(pattern).parse(newDate);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                habit.setStartDate(calendar);
                                Log.i("Error", calendar.toString());
                            }catch (Exception e){
                                Log.i("Error", "failed to set date");
                            }
                        }

                        // update date here, no suitable method
                        ElasticSearchUserController.AddUserTask addUserTask
                                = new ElasticSearchUserController.AddUserTask();
                        addUserTask.execute(user);
                        onStart();
                    }catch (Exception e){
                        Log.i("Error", "failed to change");
                    }
                }
                
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        // my code start here
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        final String tempName = pref.getString("currentUser", "");

        theName = (TextView) findViewById(R.id.name);
        theName.setText(tempName);

        Intent i = getIntent();
        index = i.getIntExtra("habit index", 0); // get index of specific habit



        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(tempName);
        try {
            user = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }

        habitsList = user.getHabitList();
        habit = habitsList.getHabit(index); // get the specific habit

        theTitle = (TextView) findViewById(R.id.showTitle);//title
        theTitle.setText(habit.getTitle());

        theReason = (TextView) findViewById(R.id.showReason);// reason
        theReason.setText(habit.getReason());

        showRepeat = (TextView) findViewById(R.id.showRepeat);
        ArrayList<Integer> frequency = new ArrayList<>(habit.getRepeatWeekOfDay());//get repeat date
        String daysInWeek = "";
        if(frequency.contains(1) == true){
            daysInWeek += " Monday ";
        }
        if(frequency.contains(2) == true){
            daysInWeek += " Tuesday ";
        }
        if(frequency.contains(3)){
            daysInWeek += " Wednesday ";
        }
        if(frequency.contains(4)){
            daysInWeek += " Thursday ";
        }
        if(frequency.contains(5)){
            daysInWeek += " Friday ";
        }
        if(frequency.contains(6)){
            daysInWeek += " Saturday ";
        }
        if(frequency.contains(7)){
            daysInWeek += " Sunday ";
        }
        showRepeat.setText(daysInWeek);

        /**
         * a tostring method to show date
         */
        theDate = (TextView) findViewById(R.id.showDate);// reason
        int year = habit.getStartDate().get(Calendar.YEAR);
        int month = habit.getStartDate().get(Calendar.MONTH) +1;
        int day = habit.getStartDate().get(Calendar.DAY_OF_MONTH);

        theDate.setText(Integer.toString(year)+"/"+ Integer.toString(month) +"/"+Integer.toString(day));

        /**
         * a listener when edit button clicked,
         *
         * implemented by using strat intent for result
         */
        Button edit = (Button) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                Intent onClickIntent = new Intent(getApplicationContext(), EditHabitActivity.class);

                onClickIntent.putExtra("habitTitle", habit.getTitle()); //title
                onClickIntent.putExtra("habitReason", habit.getReason()); //reason
                onClickIntent.putExtra("year", habit.getStartDate().get(Calendar.YEAR)); //year
                onClickIntent.putExtra("month", habit.getStartDate().get(Calendar.MONTH) +1); //month
                onClickIntent.putExtra("day", habit.getStartDate().get(Calendar.DAY_OF_MONTH)); //day

                startActivityForResult(onClickIntent,1);


            }
        });

        /**
         * a listener when states button clicked,
         *
         * implemented by using strat intent for result
         */
        Button state = (Button) findViewById(R.id.status);
        state.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
// online resource from stack overflow to get a
// count of given day:https://stackoverflow.com/questions/10428798/how-to-calculate-the-number-of-tuesday-in-one-month

                Calendar c = Calendar.getInstance();// start date
                c.set(Calendar.MONTH, habit.getStartDate().get(Calendar.MONTH));
                c.set(Calendar.YEAR, habit.getStartDate().get(Calendar.YEAR));
                Calendar today = new GregorianCalendar();

                ArrayList<Integer> frequency = new ArrayList<>(habit.getRepeatWeekOfDay());//get repeat date
                int maxDays=0;
                int th = 0;
                if(c.after(today)){ // not started
                     maxDays=0;
                     th = 0;
                    }
                else { //started
                    maxDays = today.get(Calendar.DATE) - c.get(Calendar.DATE);
                    for (int d = 1; d <= maxDays; d++) {
                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                        if (frequency.contains(dayOfWeek)) {
                            th++;
                        }
                        c.add(Calendar.DATE, 1); //next day
                    }
                }

                Intent onClickIntent = new Intent(getApplicationContext(), StatusActivity.class);
                onClickIntent.putExtra("total",th);
                onClickIntent.putExtra("total",maxDays);

                startActivity(onClickIntent);
            }
        });
    }

    @Override
    /**
     * update the content
     */
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        final String tempName = pref.getString("currentUser", "");

        theName = (TextView) findViewById(R.id.name);
        theName.setText(tempName);

        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(tempName);
        try {
            user = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }

        habitsList = user.getHabitList();
        habit = habitsList.getHabit(index); // get the specific habit

        theTitle = (TextView) findViewById(R.id.showTitle);//title
        theTitle.setText(habit.getTitle());

        theReason = (TextView) findViewById(R.id.showReason);// reason
        theReason.setText(habit.getReason());

        showRepeat = (TextView) findViewById(R.id.showRepeat);
        ArrayList<Integer> frequency = new ArrayList<>(habit.getRepeatWeekOfDay());//get repeat date
        String daysInWeek = "";
        if(frequency.contains(1) == true){
            daysInWeek += " Monday ";
        }
        if(frequency.contains(2) == true){
            daysInWeek += " Tuesday ";
        }
        if(frequency.contains(3)){
            daysInWeek += " Wednesday ";
        }
        if(frequency.contains(4)){
            daysInWeek += " Thursday ";
        }
        if(frequency.contains(5)){
            daysInWeek += " Friday ";
        }
        if(frequency.contains(6)){
            daysInWeek += " Saturday ";
        }
        if(frequency.contains(7)){
            daysInWeek += " Sunday ";
        }
        showRepeat.setText(daysInWeek);

        /**
         * a tostring method to show date
         */
        theDate = (TextView) findViewById(R.id.showDate);// reason
        int year = habit.getStartDate().get(Calendar.YEAR);
        int month = habit.getStartDate().get(Calendar.MONTH) +1;
        int day = habit.getStartDate().get(Calendar.DAY_OF_MONTH);

        theDate.setText(Integer.toString(year)+"/"+ Integer.toString(month) +"/"+Integer.toString(day));


    }
}
