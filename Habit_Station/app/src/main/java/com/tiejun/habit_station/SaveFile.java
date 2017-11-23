/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Context;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class SaveFile {
    private static final String FILENAME = "file.sav";

    public SaveFile() {}

    /**
     * Creates file.sav (Json file contains the user object)
     *
     * @param context the current context
     * @param user    the current user
     */
    public SaveFile(Context context, User user) {
        saveJson(context, user);
    }

    /**
     * Save json.
     *
     * @param context the current context
     * @param user    the current user
     */
    public void saveJson(Context context, User user) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(user, out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void saveArrayList(Context context, ArrayList<HabitEvent> habitEventArrayList, String saveFILENAME) {
        try {
            FileOutputStream fos = context.openFileOutput(saveFILENAME, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            // save the record list to Json
            Gson gson = new Gson();
            gson.toJson(habitEventArrayList, out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}