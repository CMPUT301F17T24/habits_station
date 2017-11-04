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
import android.view.View;
import android.widget.Button;

/**
 * this activity shows details about a habit
 * with button to start new intent to edit or show the status
 * fulfilled by using start for result
 *
 * so far not sure the implementation of habit list, therefore, the code
 * is not working, should be fixed when combine all the codes
 */
public class ViewHabitActivity extends AppCompatActivity {

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
                if( delSig == 0 ){
                    //update the habit information
                }
                
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        // my code start here

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
                onClickIntent.putExtra("habitName", );

                startActivityForResult(onClickIntent,1);


            }
        });






    }
}
