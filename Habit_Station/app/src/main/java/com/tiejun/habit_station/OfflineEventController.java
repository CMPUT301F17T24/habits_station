/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by tiejun on 2017-11-21.
 */

public class OfflineEventController {

    private static final String ADD_EVENT = "EventAddList.sav";
    private static final String DELETE_EVENT = "EventDeleteList.sav";
    private static final String UPDATE_EVENT = "EventUodateList.sav";

    private ArrayList<HabitEvent> eventList;

    public void AddEventTask(Context context, HabitEvent event) {

        InternetChecker checker = new InternetChecker();
        final boolean isOnline = checker.isOnline(context);
        if (!isOnline) {
            updateOfflineEventList(context, "add", event);
        }
        else {
            return;
        }
    }

    public void DeleteEventTask(Context context, HabitEvent event) {

        InternetChecker checker = new InternetChecker();
        final boolean isOnline = checker.isOnline(context);
        if (!isOnline) {
            updateOfflineEventList(context, "delete", event);
        }
        else {
            return;
        }
    }

    public void EditEventTask(Context context, HabitEvent event) {

        InternetChecker checker = new InternetChecker();
        final boolean isOnline = checker.isOnline(context);
        if (!isOnline) {
            updateOfflineEventList(context, "update", event);
        }
        else {
            return;
        }
    }



    public void updateOfflineEventList(Context context, String mode, HabitEvent event) {

        try {

            ArrayList<HabitEvent> updateList = getEventList(context, "update");
            ArrayList<HabitEvent> deleteList = getEventList(context, "delete");
            ArrayList<HabitEvent> addList = getEventList(context, "add");

            // Change accordingly
            if (mode.equals("update")) {
                updateList.add(event);
                FileOutputStream fos = context.openFileOutput(UPDATE_EVENT, Context.MODE_PRIVATE);
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
                Gson gson = new Gson();
                gson.toJson(updateList, out);
                out.flush();
                fos.close();

            } else if (mode.equals("delete")) {
               deleteList.add(event);
                FileOutputStream fos1 = context.openFileOutput(DELETE_EVENT, Context.MODE_PRIVATE);
                BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(fos1));
                Gson gson1 = new Gson();
                gson1.toJson(deleteList, out1);
                out1.flush();
                fos1.close();

            } else if (mode.equals("add")){
                addList.add(event);
                FileOutputStream fos = context.openFileOutput(ADD_EVENT, Context.MODE_PRIVATE);
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
                Gson gson = new Gson();
                gson.toJson(addList, out);
                out.flush();
                fos.close();
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public ArrayList<HabitEvent> getEventList(Context context, String mode){
        try {
            String filename;
            if (mode.equals("add")){
                filename = ADD_EVENT;
            } else if (mode.equals("delete")){
                filename = DELETE_EVENT;
            } else if (mode.equals("update")){
                filename = UPDATE_EVENT;
            } else{
                return null;
            }

            FileInputStream fis = context.openFileInput(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<HabitEvent>>(){}.getType();
            eventList = gson.fromJson(in, type); // deserializes json into ArrayList<String>
            fis.close();
            return eventList;
        } catch (FileNotFoundException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        } catch (Exception e) {
            return null;
        }

    }
}

