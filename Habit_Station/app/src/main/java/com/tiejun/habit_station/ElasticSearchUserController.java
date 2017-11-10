/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;
import android.os.AsyncTask;
import android.util.Log;

import com.tiejun.habit_station.User;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;


public class ElasticSearchUserController {
    private static JestDroidClient client;


    /**
     * The function which add user to elastic search
     */
    public static class AddUserTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user : users) {
                Index index = new Index.Builder(user).index("cmput301f17t24").type("user").id(user.getName()).build();

                try {
                    // where is the client
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        Log.d("In AsyncTask ID", result.getId());
                        //user.setAid(result.getId());
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
    public static class GetUserTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            verifySettings();

            User user = new User();
            Get get = new Get.Builder("cmput301f17t24", params[0]).type("user").build();

            try{
                JestResult result = client.execute(get);
                user = result.getSourceAsObject(User.class);
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return user;
        }

    }

    /**
     * The function which update user from elastic search
     */
    public static class UpdateUserTask extends  AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user : users) {
                Update update = new Update.Builder(user).index("cmput301f17t24").type("user").id(user.getName()).build();
                //Delete delete = new Delete.Builder(user.getName()).index("cmput301f17t24").type("user").build();


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
     * The function to judge if the user is stored in elastic search
     */
    public static class IsExist extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params){
            verifySettings();

            User user = new User();

            Get get = new Get.Builder("cmput301f17t24", params[0]).type("user").build();
            Log.d("usertest", params[0]);

            try {
                JestResult result = client.execute(get);
                user = result.getSourceAsObject(User.class);
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            if (user == null) {
                return false;
            }

            return true;
        }
    }

    /**
     * Create new Index with empty data
     */
//    public static void createIndex() {
//        verifySettings();
//        try {
//            boolean indexExists = client.execute(new IndicesExists.Builder("cmput301f17t24").build()).isSucceeded();
//            if (indexExists) {
//                client.execute(new DeleteIndex.Builder("cmput301f17t24").build());
//            }
//            client.execute(new CreateIndex.Builder("cmput301f17t24").build());
//        } catch (Exception e) {
//            Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
//        }
//    }




    // TODO we need a function which gets tweets from elastic search
    public static class GetHitoryTask extends AsyncTask<String, Void, ArrayList<HabitEvent>> {
        @Override
        protected ArrayList<HabitEvent> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<HabitEvent> events = new ArrayList<HabitEvent>();

            // TODO Build the query
            Search search = new Search.Builder(search_parameters[0])
                    .addIndex("cmput301f17t24")
                    .addType("user")

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
