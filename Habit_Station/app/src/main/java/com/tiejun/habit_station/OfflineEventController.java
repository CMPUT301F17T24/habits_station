/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * handle offline behaviour with database
 */

public class OfflineEventController {

    private static final String FILEPREFIX = "offline_";
    private static String eventID;
    Context context;
    ArrayList<OfflineEventModule> offlineEventArrayList;

    public OfflineEventController(String eventID, Context context) {
        this.context = context;
        loadList(eventID);
    }

    private void loadList(String eventID) {

        this.eventID = eventID;
        File cashedFile  = new File(context.getExternalCacheDir(),FILEPREFIX + eventID);

        if (!cashedFile.exists()) {
            offlineEventArrayList = new ArrayList<OfflineEventModule>();
        }

        else {
            ReadFile(cashedFile);
        }
    }

    public void addOfflineAction(String action, HabitEvent event) {
        OfflineEventModule offlineEvent = new OfflineEventModule();
        offlineEvent.action = action;
        offlineEvent.event = event;

        offlineEventArrayList.add(offlineEvent);
        SaveFile();
    }

    public int getOfflineEventListSize() {
        return offlineEventArrayList.size();
    }

    public ArrayList<OfflineEventModule> getOfflineEventArrayList() {
        return offlineEventArrayList;
    }

    public boolean syncOfflineEvent() {

        if (getOfflineEventListSize() == 0) {
            return true;
        }

        for (OfflineEventModule offlineEventModule : offlineEventArrayList) {
            doSync(offlineEventModule);
        }

        File cachedFile = new File(context.getExternalCacheDir(), FILEPREFIX + eventID);
        cachedFile.delete();

        Log.i("Offline", "Offline Data synced.");
        Toast.makeText(context,
                "Offline Data synced.",
                Toast.LENGTH_SHORT).show();
        return true;
    }

    public void clearFile(String eventID) {
        File cachedFile = new File(context.getExternalCacheDir(), FILEPREFIX + eventID);
        cachedFile.delete();
    }

    private void doSync(OfflineEventModule offlineEventModule) {

        if (offlineEventModule.action.equals("add")) {
            ElasticSearchEventController.AddEventTask addEventTask
                    = new ElasticSearchEventController.AddEventTask();
            addEventTask.execute(offlineEventModule.event);
        }

        else if (offlineEventModule.action.equals("delete")) {
            ElasticSearchEventController.DeleteEventTask deleteEventTask
                    = new ElasticSearchEventController.DeleteEventTask();
            deleteEventTask.execute(offlineEventModule.event);
        }

        else if (offlineEventModule.action.equals("update")) {
            ElasticSearchEventController.DeleteEventTask deleteEventTask
                    = new ElasticSearchEventController.DeleteEventTask();
            deleteEventTask.execute(offlineEventModule.event);

            ////// add new event ///////
            ElasticSearchEventController.AddEventTask addEventTask
                    = new ElasticSearchEventController.AddEventTask();
            addEventTask.execute(offlineEventModule.event);
        }
    }

    private void ReadFile(File fp) {
        try {
            FileInputStream fis = new FileInputStream(fp);

            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            offlineEventArrayList = gson.fromJson(in, new TypeToken<ArrayList<OfflineEventModule>>() {}.getType());
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SaveFile() {
        try {

            File cachedfile = new File(context.getExternalCacheDir(), FILEPREFIX + eventID);
            FileOutputStream outputStream = new FileOutputStream(cachedfile);
            //outputStream = context.openFileOutput(OFFLINE_NAME, Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream));
            Gson gson = new Gson();

            gson.toJson(offlineEventArrayList, out);
            out.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Log.i("Offline", "Offline Data Saved, Count: " + getOfflineListSize());
    }
}
