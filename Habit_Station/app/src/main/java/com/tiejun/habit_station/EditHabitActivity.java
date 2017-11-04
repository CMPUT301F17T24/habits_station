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
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.format;

public class EditHabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        /**
         * set a click listener for edit title
         */
        final EditText editTitle = (EditText) findViewById(R.id.editTitle);
        editTitle.setHint("old title");
        editTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                String newTitle = editTitle.getText().toString();// get the new title

            }
        });

        /**
         * set a click listener for edit date
         */
        final EditText editDate = (EditText) findViewById(R.id.editDate);
        editDate.setHint("old date");
        editDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                    Date newDate = format.parse(editDate.getText().toString());// get the new title
                }catch (ParseException ex){
                    Date newDate = new Date(); // handle the exception by assign current date
                }

            }
        });

        /**
         * set a click listener for edit reason
         */
        final EditText editReason = (EditText) findViewById(R.id.editReason);
        editReason.setHint("old reason");
        editReason.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                String newReason = editReason.getText().toString();// get the new title

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

                setResult(RESULT_OK, backIntent);
                finish();
            }
        });


    }
    


}
