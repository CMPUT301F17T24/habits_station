/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Activity to show habit event library
 *
 * @author xuanyi
 * @version 1.0
 *
 */

public class HabitEventLibraryActivity extends AppCompatActivity {

    private TextView title;
    private ListView events;
    private ArrayList<HabitEvent> fillist  = new ArrayList<HabitEvent>();
    protected ArrayAdapter<HabitEvent> adapter;
    private int click_item_index=-1;
    String habit_name;
    String event_query;

    //private static final String FILENAME = "habitEventLibrary.sav";// for save and load
    private String FILENAME  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_library);
        Intent intent = getIntent();
        habit_name = intent.getStringExtra("habit name");


        events = (ListView)findViewById(R.id.events);
        Button addButton = (Button) findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(HabitEventLibraryActivity.this, EditHabitEventActivity.class);
                intent.putExtra("habit name",habit_name);
                intent.putExtra("select", click_item_index);
                startActivity(intent);

            }
        });

        registerForContextMenu(events);
        events.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                click_item_index=position;
                return false;
            }
        });



        ImageView home_tab = (ImageView) findViewById(R.id.home);
        home_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitEventLibraryActivity.this,  MainPageActivity.class);
                startActivity(intent);
            }
        });


        ImageView lib_tab = (ImageView) findViewById(R.id.library);
        lib_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitEventLibraryActivity.this,  HabitLibraryActivity.class);
                startActivity(intent);
            }
        });


    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "View Event details");
        menu.add(0, v.getId(), 0, "Edit Events");
        //menu.add(0, v.getId(), 0, "Delete");

    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        if (item.getTitle().equals("View Event details")) {
            Intent i = new Intent(HabitEventLibraryActivity.this, ViewEventActivity .class);
            i.putExtra("habit name",habit_name);
            i.putExtra("select", click_item_index);
            i.putExtra("query",event_query);
            startActivity(i);
        }
        else if (item.getTitle().equals("Edit Events")) {
            Intent i = new Intent(HabitEventLibraryActivity.this, EditHabitEventActivity .class);
            i.putExtra("habit name",habit_name);
            i.putExtra("select", click_item_index);
            i.putExtra("query",event_query);
            startActivity(i);
        }

        else {
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");
        /**
         * a offline behaviour handler code
         * use local buffer to show
         */
        if( isNetworkAvailable(this) == false){
            click_item_index = -1;
            Toast.makeText(getApplicationContext(), "You are now in offline mode.", Toast.LENGTH_SHORT).show();
            FILENAME = userName+ habit_name +".sav";
            loadFromFile();

            adapter = new ArrayAdapter<HabitEvent>(this, R.layout.list_habits, fillist);
            events.setAdapter(adapter);

            title = (TextView) findViewById(R.id.title);
            title.setText(habit_name + " Library");

        }

        else {  // start of online block
            click_item_index = -1;
            userName = pref.getString("currentUser", "");

            Intent intent = getIntent();
            String habit_name = intent.getStringExtra("habit name");

            fillist.clear();
            FILENAME = userName+habit_name +".sav";
            loadFromFile();

            Log.d("file name:", FILENAME);

            if (fillist.size()!=0){  //load from file if file not null

                for (HabitEvent element: fillist){

                    ElasticSearchEventController.AddEventTask addEventTask
                            = new ElasticSearchEventController.AddEventTask();
                    addEventTask.execute(element);
                }
            }
            else {   // elasticsearch

                event_query = "{\n" +
                        "  \"query\": { \n" +
                        "\"bool\": {\n" +
                        "\"must\": [\n" +
                        "{" + " \"term\" : { \"uName\" : \"" + userName + "\" }},\n" +
                        "{" + " \"match\" : {  \"eName\" : \"" + habit_name + "\" }}\n" +
                        "]" +
                        "}" +
                        "}" +
                        "}";

                ElasticSearchEventController.GetEvents getHEvent
                        = new ElasticSearchEventController.GetEvents();
                getHEvent.execute(event_query);

                try {
                    fillist.clear();
                    fillist.addAll(getHEvent.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            adapter = new ArrayAdapter<HabitEvent>(this, R.layout.list_habits, fillist);
            events.setAdapter(adapter);

            title = (TextView) findViewById(R.id.title);
            title.setText(habit_name + " Library");
            saveInFile();

            //////////// synchronize ///////
            ArrayList<HabitEvent>  result = new ArrayList<HabitEvent>();
            String event_query = "{\n" +
                    "  \"query\": { \n" +
                    "\"bool\": {\n"+
                    "\"must\": [\n"+
                    "{"+ " \"term\" : { \"uName\" : \"" + userName +  "\" }},\n" +
                    "{"+ " \"match\" : {  \"eName\" : \"" + habit_name +  "\" }}\n" +
                    "]"+
                    "}"+
                    "}"+
                    "}";
            ElasticSearchEventController.GetEvents getHEvent
                    = new  ElasticSearchEventController.GetEvents();
            getHEvent.execute(event_query);

            try {
                result.clear();
                result.addAll(getHEvent.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            ////////// update  //////
            for (HabitEvent element: result){
                ElasticSearchEventController.DeleteEventTask deleteEventTask
                        = new ElasticSearchEventController.DeleteEventTask();
                deleteEventTask.execute(element);

            }
            for (HabitEvent element: fillist){
                ElasticSearchEventController.AddEventTask addEventTask
                        = new ElasticSearchEventController.AddEventTask();
                addEventTask.execute(element);

            }
            ///////// finish synchronization //////////

        }


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


    /**
     * a method to save in file
     * source: https://github.com/wooloba/lonelyTwitter/blob/master/app/src/main/java/ca/ualberta/cs/lonelytwitter/LonelyTwitterActivity.java
     * from old lab excercise
     */
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(fillist, writer);
            writer.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();

            //e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
            //e.printStackTrace();
        }
    }

    /**
     * a method to load from file
     * source: https://github.com/wooloba/lonelyTwitter/blob/master/app/src/main/java/ca/ualberta/cs/lonelytwitter/LonelyTwitterActivity.java
     * from old lab excercise
     */
    private void loadFromFile() {
        //ArrayList<String> tweets = new ArrayList<String>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<HabitEvent>>(){}.getType();
            fillist = gson.fromJson(in, listType);



            //String line = in.readLine();
            //while (line != null) {
            //tweets.add(line);
            //line = in.readLine();

        } catch (FileNotFoundException e) {
            //TODO Auto-generated catch block
            fillist = new ArrayList<HabitEvent>();
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
            //e.printStackTrace();
        }
        //return tweets.toArray(new String[tweets.size()]);

    }





}


