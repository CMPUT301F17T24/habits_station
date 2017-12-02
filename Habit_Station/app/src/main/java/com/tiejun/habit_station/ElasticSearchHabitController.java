/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify this code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

/**
 * Elastic search controller for habits
 *
 * @author xuanyi
 * @version 1.0
 * @see Habit
 *
 */

public class ElasticSearchHabitController {

    private static JestDroidClient client;


    /**
     * The function which add habit to elastic search
     */
    public static class AddHabitTask extends AsyncTask<Habit, Void, Void> {

        @Override
        protected Void doInBackground(Habit... habits) {
            verifySettings();

            for (Habit habit : habits) {
                Index index = new Index.Builder(habit).index("cmput301f17t24").type("habit")
                        .id(habit.getuName() + habit.getTitle().toUpperCase())
                        .build();

                try {
                    // where is the client
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        Log.d("In AsyncTask ID", result.getId());
                    } else {
                        Log.i("Error", "Elasticsearch was not able to add the user.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Error", "The application failed to build and send the user");
                }

            }
            return null;
        }
    }

    /**
     * The function which delete habit from elastic search
     */
    public static class DeleteHabitTask extends AsyncTask<Habit, Void, Void> {

        @Override
        protected Void doInBackground(Habit... habits) {
            verifySettings();

            for (Habit habit : habits) {
                Delete delete = new Delete.Builder(habit.getuName() + habit.getTitle().toUpperCase())
                        .index("cmput301f17t24").type("habit").build();

                try {
                    // where is the client
                    DocumentResult result = client.execute(delete);
                    if (result.isSucceeded()) {
                        Log.d("In AsyncTask ID", result.getId());
                    } else {
                        Log.i("Error", "Elasticsearch was not able to delete the user.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Error", "The application failed to build and send the user");
                }

            }
            return null;
        }
    }

    /**
     * The function which update habit in elastic search
     */
    public static class UpdateHabitTask extends  AsyncTask<Habit, Void, Void> {
        @Override
        protected Void doInBackground(Habit... habits) {
            verifySettings();

            for (Habit habit : habits) {
                Update update = new Update.Builder(habit).index("cmput301f17t24").type("habit").id(habit.getuName() + habit.getTitle().toUpperCase()).build();


                try {
                    // where is the client
                    DocumentResult result = client.execute(update);
                    if (result.isSucceeded()) {
                        Log.d("In AsyncTask ID", result.getId());
                    } else {
                        Log.i("Error", "Elasticsearch was not able to update the user.");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the user");
                }
            }
            return null;
        }
    }

    /**
     * The function which gets habit from elastic search
     */
    public static class GetHabitTask extends AsyncTask<String, Void, Habit> {
        @Override
        protected Habit doInBackground(String... params) {
            verifySettings();

            Habit habit = new Habit();
            Get get = new Get.Builder("cmput301f17t24", params[0]).type("habit").build();

            try{
                JestResult result = client.execute(get);
                habit = result.getSourceAsObject(Habit.class);
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return habit;
        }

    }

    /**
     * The function which check if habit is already existed in elastic search
     */
    public static class IsExist extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params){
            verifySettings();

            Habit habit = new Habit();
            Get get = new Get.Builder("cmput301f17t24", params[0]).type("habit").build();


            try {
                JestResult result = client.execute(get);
                habit = result.getSourceAsObject(Habit.class);
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            if (habit == null) {
                return false;
            }

            return true;
        }
    }


    /**
     * The function which get a array list of habits from elastic search
     */
    public static class GetHabits extends AsyncTask<String, Void, ArrayList<Habit>> {
        @Override
        protected ArrayList<Habit> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Habit> habits = new ArrayList<Habit>();

            // TODO Build the query
            Search search = new Search.Builder(search_parameters[0])
                    .addIndex("cmput301f17t24")
                    .addType("habit")
                    .build();

            try {
                // TODO get the results of the query
                //Log.d("AAA",String.valueOf(search_parameters[0]));
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<Habit> foundHistories
                            =result.getSourceAsObjectList(Habit.class);
                    //List<SearchResult.Hit<NormalTweet, Void>> hits = result.getHits(NormalTweet.class);

                    habits.addAll(foundHistories);

                }
                else{
                    Log.e("Error","Query failed");
                }
            }
            catch (Exception e) {
                Log.e("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return habits;
        }
    }

    /**
     * Verify settings
     */
    public static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }



}
