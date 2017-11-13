/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class HabitEventLibraryActivity extends AppCompatActivity {

    private TextView title;
    private ListView events;
    private ArrayList<HabitEvent> fillist  = new ArrayList<HabitEvent>();
    protected ArrayAdapter<HabitEvent> adapter;
    private int click_item_index=-1;
    String habit_name;
    String event_query;

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

        /*else if (item.getTitle() == "Delete") {
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();

            SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
            String userName = pref.getString("currentUser", "");

            HabitEvent selected_event = fillist.get(position);

            ElasticSearchEventController.DeleteEventTask deleteEventTask
                    = new ElasticSearchEventController.DeleteEventTask();
            deleteEventTask.execute(selected_event);

            Toast.makeText(getApplicationContext(), "Successfully deleted the event! ", Toast.LENGTH_SHORT).show();

            onStart();
        }
*/


        else {
            return false;
        }
        return true;
    }



    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        click_item_index = -1;
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");

        Intent intent = getIntent();
        String habit_name = intent.getStringExtra("habit name");

        Log.d("event", "library");
        Log.d("username", userName);
        Log.d("habitname is", habit_name);


        event_query = "{\n" +
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
            fillist.clear();
            fillist.addAll(getHEvent.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        adapter = new ArrayAdapter<HabitEvent>(this, R.layout.list_habits, fillist);
        events.setAdapter(adapter);

        title = (TextView) findViewById(R.id.title);
        title.setText(habit_name + " Library");


    }

}


