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
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {
    protected EditText username;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

       username = (EditText) findViewById(R.id.username);

        Button signIn = (Button) findViewById(R.id.signin);
        signIn.setOnClickListener( new View.OnClickListener (){

            public void onClick(View v) {
                userName = username.getText().toString();
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                intent.putExtra("test", userName);
                startActivity(intent);
            }
        });

        Button signUp = (Button) findViewById(R.id.signup);
        signUp.setOnClickListener( new View.OnClickListener (){

            public void onClick(View v) {
                Log.d("sign up", userName);
            }
        });
    }





}
