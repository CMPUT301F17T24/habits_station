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
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.tiejun.habit_station.R.id.date;

/**
 * this activity shows details about a habit
 * with button to start new intent to edit or show the status
 * fulfilled by using start for result
 *
 */
public class ViewHabitActivity extends AppCompatActivity {

    protected HabitList habitsList =new HabitList();
    private TextView theTitle;
    private TextView theName;
    private TextView theReason;
    private TextView theDate;
    private Habit habit;
    private int index;


    /**
     * online resource from stack overflow of pass variable back to main activity
     *
     * modified to start a new intent of edit habit
     *
     * requestcode 1 for edit and delete
     *
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // edit or delete habit
            if(resultCode == RESULT_OK) {
                // set variable of edited data here
                int delSig =data.getIntExtra("delSig",0); //get signal of delete
                if( delSig == 1 ){
                    //delete the habit
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


        User user = new User();
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

        /**
         * this method convert date to string,
         * use online resource from stack overflow:
         * https://stackoverflow.com/questions/5683728/convert-java-util-date-to-string
         */
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //Or whatever format fits best your needs.
//        String dateStr = sdf.format(habit.getStartDate());

        theDate = (TextView) findViewById(R.id.showDate);// reason
        theDate.setText(habit.getStartDate().toString());

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
                // put extra here
                //onClickIntent.putExtra("habitName", );

                startActivityForResult(onClickIntent,1);


            }
        });






    }
}
