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

public class HabitEventLibraryActivity extends AppCompatActivity {

    private TextView title;
    private ListView events;
    protected HabitEventList habitEventList = new HabitEventList();
    protected ArrayAdapter<HabitEvent> adapter;
    private int click_item_index=-1;
    int habitIndex=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_library);


        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");
        Intent intent = getIntent();
        //final int habitIndex = intent.getIntExtra("habit index", 0);
        habitIndex = intent.getIntExtra("habit index", 0);

        //title = (TextView) findViewById(R.id.title);

        events = (ListView)findViewById(R.id.events);
        Button addButton = (Button) findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(HabitEventLibraryActivity.this, EditHabitEventActivity.class);
                intent.putExtra("habit index", habitIndex);

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
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });


        ImageView lib_tab = (ImageView) findViewById(R.id.library);
        lib_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitEventLibraryActivity.this,  HabitLibraryActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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
        menu.add(0, v.getId(), 0, "Delete");

    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        if (item.getTitle().equals("View Event details")) {
            Intent i = new Intent(HabitEventLibraryActivity.this, ViewEventActivity .class);
            i.putExtra("event index", position);
            startActivity(i);
        }
        else if (item.getTitle().equals("Edit Events")) {
            Intent i = new Intent(HabitEventLibraryActivity.this, EditHabitEventActivity .class);
            i.putExtra("habit index", habitIndex);
            i.putExtra("select", click_item_index);            startActivity(i);
        }

        else if (item.getTitle() == "Delete") {
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();

            SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
            String userName = pref.getString("currentUser", "");
            User user = new User();
            ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
            getUserTask.execute(userName);
            try {
                user = getUserTask.get();
            } catch (Exception e) {
                Log.i("Error", "Failed to get the User out of the async object");
            }

            habitEventList = user.getHabitList().getHabit(habitIndex).getHabitEventList();
            habitEventList.delete(habitEventList.getEvent(click_item_index));

            ElasticSearchUserController.AddUserTask addUserTask
                    = new ElasticSearchUserController.AddUserTask();
            addUserTask.execute(user);
            onStart();
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
        click_item_index = -1;
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");

        Intent intent = getIntent();
        habitIndex = intent.getIntExtra("habit index", 0);

        //Log.d("user_habL_start",userName);

        User user = new User();
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(userName);
        try {
            user = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }



        habitEventList = user.getHabitList().getHabit(habitIndex).getHabitEventList();
        adapter = new ArrayAdapter<HabitEvent>(this, R.layout.list_habits, habitEventList.sortEvents());
        events.setAdapter(adapter);

        title = (TextView) findViewById(R.id.title);
        String habit_name  = user.getHabitList().getHabit(habitIndex).getTitle();
        title.setText(habit_name + " Library");


    }





}
