/*
 * Copyright (c) 2017 Team24, CMPUT301, University of Alberta - All Rights Reserved.
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
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.format;


/**
 * a activity when click edit, implemented by back intent to update information
 *
 * @author yfeng
 * @version 1.0
 *
 */
public class EditHabitActivity extends AppCompatActivity {
    private String oldTitle;
    private String oldReason;
    private String newTitle;
    private String newReason;
    private String newDate;
    private int year;
    private int month;
    private int day;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);
        Intent i = getIntent();
        oldTitle = i.getStringExtra("habitTitle");
        oldReason = i.getStringExtra("habitReason");
        year = i.getIntExtra("year",0);
        month = i.getIntExtra("month",0);
        day = i.getIntExtra("day",0);
        
        /**
         * set a click listener for edit title
         */
        final EditText editTitle = (EditText) findViewById(R.id.editTitle);
        editTitle.setHint(oldTitle);
        editTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
            }
        });
        
        /**
         * set a click listener for edit date
         */
        final EditText editDate = (EditText) findViewById(R.id.editDate);
        editDate.setHint(Integer.toString(year)+"/"+ Integer.toString(month) +"/"+Integer.toString(day));
        editDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                //newDate = editDate.getText().toString(); // get new date
                
            }
        });
        
        /**
         * set a click listener for edit reason
         */
        final EditText editReason = (EditText) findViewById(R.id.editReason);
        editReason.setHint(oldReason);
        editReason.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
            }
        });
        
        /**
         *  set a click listener for delete
         *
         *  the back intent send back a signal 1, indicate the delete
         */
        final Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                
                Intent backIntent = new Intent();
                backIntent.putExtra("delSig", 1); //
                
                setResult(RESULT_OK, backIntent);
                finish();
            }
        });

        //code for edit frequency, keep the block

        final ArrayList<Integer> frequency = new ArrayList<>();//get repeat date
        final Button mon = (Button) findViewById(R.id.mon);
        mon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                mon.setBackgroundResource(R.drawable.m1_bt);
                frequency.add(1);

            }
        });
        final Button tue = (Button) findViewById(R.id.tue);
        tue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                tue.setBackgroundResource(R.drawable.tu1_bt);
                frequency.add(2);

            }
        });
        final Button wed = (Button) findViewById(R.id.wed);
        wed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                wed.setBackgroundResource(R.drawable.w1_bt);
                frequency.add(3);

            }
        });
        final Button fri = (Button) findViewById(R.id.fri);
        fri.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                fri.setBackgroundResource(R.drawable.fri1_bt);
                frequency.add(5);

            }
        });
        final Button thur = (Button) findViewById(R.id.thur);
        thur.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                thur.setBackgroundResource(R.drawable.th1_bt);
                frequency.add(4);

            }
        });
        final Button sat = (Button) findViewById(R.id.sat);
        sat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                sat.setBackgroundResource(R.drawable.sa1_bt);
                frequency.add(6);

            }
        });
        final Button sun = (Button) findViewById(R.id.sun);
        sun.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                sun.setBackgroundResource(R.drawable.sun1_bt);
                frequency.add(0);
            }
        });

        // keep the block
        
        /**
         *  set a click listener for edit
         *
         *  the back intent send back a signal 0, indicate the edit
         */
        final Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                
                Intent backIntent = new Intent();
                backIntent.putExtra("delSig", 0);
                newReason = editReason.getText().toString();// get the new reason
                newTitle = editTitle.getText().toString();// get the new title
                newDate = editDate.getText().toString(); // get new date
                //Log.i("Error",newReason);
                if( newTitle.length() == 0){
                    newTitle = oldTitle;
                }
                if( newReason.length() == 0){
                    newReason = oldReason;
                }
                if( newDate.length() == 0){
                    newDate = "no change";
                }
                backIntent.putExtra("newReason",newReason);
                backIntent.putExtra("newTitle",newTitle);
                backIntent.putExtra("newDate",newDate);
                backIntent.putExtra("newRepeat",frequency);
                setResult(RESULT_OK, backIntent);
                finish();



            }
        });
        
        
    }
    @Override
    public void onBackPressed(){
        setResult(RESULT_OK);

        Intent backIntent = new Intent();
        backIntent.putExtra("delSig", 3);
        setResult(RESULT_OK, backIntent);
        finish();

    }
    
    
    
}