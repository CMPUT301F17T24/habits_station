/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Intent;
import android.graphics.drawable.RotateDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class StatusActivity extends AppCompatActivity {
    private int eventDone;
    private int totalDays;
    private int progress;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        // my code starts here
        Intent i = getIntent();
        eventDone = i.getIntExtra("complete",0);
        totalDays = i.getIntExtra("total",0);

        Log.d("complete",String.valueOf(eventDone));
        Log.d("total",String.valueOf(totalDays));

        if (totalDays ==0){
            progress =100;
        }
        else{
            progress = eventDone/totalDays*100;

        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setProgress(progress);



        final Button cool = (Button) findViewById(R.id.cool);
        cool.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                Intent onClickIntent = new Intent(getApplicationContext(), ViewHabitActivity.class);

                startActivity(onClickIntent);
            }
        });


    }
}