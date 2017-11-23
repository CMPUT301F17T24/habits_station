/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * This is a method class, the main purpose is to load the UserList From a Json file.
 * (Version 1.0 contains load file locally only, load from online function will be added later.)
 * To use this class,use the following steps:
 * (need to declare: [private UserList userList;] on top)
 * 0. Context context = getApplicationContext();
 * 1. LoadFile load = new LoadFile();
 * 2. user = load.loadUser(context);
 */
public class LoadFile {
    private static final String FILENAME = "file.sav";
    private static final String FILENAME1 = "filter.sav";
    private User user;

    /**
     * Instantiates a new Load file.
     */
    public LoadFile(){}

    /**
     * Load CurrentUser Json file
     *
     * @param context the context
     * @return the user
     */
    public User loadUser(Context context) {
        try {
            //Taken fron https://static.javadoc.io/com.google.code.gson/gson/2.6.2/com/google/gson/Gson.html
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type type = new TypeToken<User>(){}.getType();
            user = gson.fromJson(in, type); // deserializes json into User
            fis.close();
            return user;
        } catch (FileNotFoundException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }


    public ArrayList<HabitEvent> loadFilteredMoodEventList(Context context) {

        ArrayList<HabitEvent> habitEventList;

        try {
            FileInputStream fis = context.openFileInput(FILENAME1);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            habitEventList = gson.fromJson(in, new TypeToken<ArrayList<HabitEvent>>(){}.getType());
            fis.close();

            return habitEventList;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}