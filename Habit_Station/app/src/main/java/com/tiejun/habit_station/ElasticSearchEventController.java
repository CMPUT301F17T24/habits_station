/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
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

import static com.tiejun.habit_station.ElasticSearchUserController.verifySettings;

/**
 * Created by XuanyiWu on 2017-11-08.
 */

public class ElasticSearchEventController {

    private static JestDroidClient client;


    /**
     * The function which add event to elastic search
     */
    public static class AddEventTask extends AsyncTask<HabitEvent, Void, Void> {

        @Override
        protected Void doInBackground(HabitEvent... events) {
            verifySettings();

            for (HabitEvent event : events) {
                Index index = new Index.Builder(event).index("cmput301f17t24").type("event").id(event.getuName()+event.geteName()
                        +event.geteTime().get(Calendar.YEAR)
                        +String.valueOf(event.geteTime().get(Calendar.MONTH)+1)
                        +event.geteTime().get(Calendar.DAY_OF_MONTH) ).build();

                try {
                    // where is the client
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        Log.d("In AsyncTask ID", result.getId());
                    } else {
                        Log.i("Error", "Elasticsearch was not able to add the event.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Error", "The application failed to build and send the event");
                }

            }
            return null;
        }
    }



    /**
     * The function which add event to elastic search
     */
    public static class DeleteEventTask extends AsyncTask<HabitEvent, Void, Void> {

        @Override
        protected Void doInBackground(HabitEvent... events) {
            verifySettings();

            for (HabitEvent event : events) {
                Delete delete = new Delete.Builder(event.getuName()+event.geteName()
                        +event.geteTime().get(Calendar.YEAR)
                        +String.valueOf(event.geteTime().get(Calendar.MONTH)+1)
                        +event.geteTime().get(Calendar.DAY_OF_MONTH))
                        .index("cmput301f17t24").type("event").build();


                try {
                    // where is the client
                    DocumentResult result = client.execute(delete);
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
     * The function which add event to elastic search
     */
    public static class UpdateEventTask extends AsyncTask<HabitEvent, Void, Void> {

        @Override
        protected Void doInBackground(HabitEvent... events) {
            verifySettings();

            for (HabitEvent event : events) {
                Update update = new Update.Builder(event).index("cmput301f17t24").type("event").id(event.getuName()+event.geteName()
                        +event.geteTime().get(Calendar.YEAR)
                        +String.valueOf(event.geteTime().get(Calendar.MONTH)+1)
                        +event.geteTime().get(Calendar.DAY_OF_MONTH) ).build();

                try {
                    // where is the client
                    DocumentResult result = client.execute(update);
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
     * The function which get user from elastic search
     */
    public static class GetEventTask extends AsyncTask<String, Void, HabitEvent> {
        @Override
        protected HabitEvent doInBackground(String... params) {
            verifySettings();

            HabitEvent event = new HabitEvent();
            Get get = new Get.Builder("cmput301f17t24", params[0]).type("event").build();

            try{
                JestResult result = client.execute(get);
                event = result.getSourceAsObject(HabitEvent.class);
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return event;
        }

    }


    /**
     * The function to judge if the user is stored in elastic search
     */
    public static class IsExist extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params){
            verifySettings();

            HabitEvent event = new HabitEvent();
            Get get = new Get.Builder("cmput301f17t24", params[0]).type("event").build();

            Log.d("eventtest", params[0]);

            try {
                JestResult result = client.execute(get);
                event = result.getSourceAsObject(HabitEvent.class);
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            if (event == null) {
                return false;
            }

            return true;
        }
    }




    // TODO we need a function which gets tweets from elastic search
    public static class GetEvents extends AsyncTask<String, Void, ArrayList<HabitEvent>> {
        @Override
        protected ArrayList<HabitEvent> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<HabitEvent> events = new ArrayList<HabitEvent>();

            // TODO Build the query
            Search search = new Search.Builder(search_parameters[0])
                    .addIndex("cmput301f17t24")
                    .addType("event")

                    .build();

            try {
                // TODO get the results of the query
                //Log.d("AAA",String.valueOf(search_parameters[0]));
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<HabitEvent> foundHistories
                            =result.getSourceAsObjectList(HabitEvent.class);
                    //List<SearchResult.Hit<NormalTweet, Void>> hits = result.getHits(NormalTweet.class);

                    events.addAll(foundHistories);

                }
                else{
                    Log.e("Error","Query failed");
                }
            }
            catch (Exception e) {
                Log.e("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return events;
        }
    }







    /**
     * Verify settings.
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
