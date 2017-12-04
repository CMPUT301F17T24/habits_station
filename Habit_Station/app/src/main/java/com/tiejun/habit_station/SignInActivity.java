/*
 * Copyright (c) 2017 Team24, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact xtie@ualberta.ca.
 */

package com.tiejun.habit_station;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Sign in activity
 *
 * @author xtie
 * @version 1.0
 *
 */
public class SignInActivity extends AppCompatActivity {
    public UserList userList;
    public String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        userList = new UserList();

        Button signIn = (Button) findViewById(R.id.signin);
        Button signUp = (Button) findViewById(R.id.signup);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView testView = (TextView) findViewById(R.id.username);
                userName = testView.getText().toString();

                if( isNetworkAvailable(getApplication()) == false){
                    SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                    String lastUserName = pref.getString("currentUser", "");
                    // only the last user is allowed to do the offline login
                    if (userName.equals(lastUserName)){
                        Intent intent = new Intent(SignInActivity.this, MainPageActivity.class);
                        Log.d("username", userName);
                        storePreference(userName);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Offline login.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Check your network.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {           // online
                    if (!userName.equals(userName.toLowerCase())) {
                        testView.setError("NO Capital letter for user name. ");
                    } else if (existedUser(userName)) {
                        Intent intent = new Intent(SignInActivity.this, MainPageActivity.class);
                        Log.d("username", userName);
                        storePreference(userName);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "online login.", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Check if the user has already created an account
     *
     * @param name username
     * @return
     */
    private boolean existedUser (String name) {
        ElasticSearchUserController.IsExist isExist = new ElasticSearchUserController.IsExist();
        isExist.execute(name);

        try {
            if (isExist.get()) {
                return true;
            } else {
                Toast.makeText(getApplicationContext(), name + " does not exist.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * This method stored the successfully login user's name in a local file
     * File explorer -> data -> data -> com.example.mac.bugfree -> sharef_prefs -> data.xml
     *
     * @param name String of user name to be stored
     */
    private void storePreference(String name){
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("currentUser",name);
        editor.apply();
    }


    /**
     * a offline detecter
     * Source: https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
     * @param c
     * @return
     */
    private boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




}
