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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class HabitLibraryActivity extends AppCompatActivity {

    private ListView habitList;

    protected HabitList habits =new HabitList();

    protected ArrayAdapter<Habit> adapter;

    private int click_item_index;

    private ArrayList<Habit> fillist  = new ArrayList<Habit>();

    private Habit selectedHabit = new Habit();   // used to delete

    private String hName ;
    //private String MYhabits;

    //private String userName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_library);



//        selectedHabit = fillist.get(position);
//        hName = selectedHabit.getTitle();

        Log.d("Back", "create");

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");

        habitList = (ListView)findViewById(R.id.habits);
        Button addButton = (Button) findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                //Log.d("user_habL",userName);
                Intent intent = new Intent(HabitLibraryActivity.this, AddHabitActivity.class);
                startActivity(intent);

            }
        });



        ImageView home_tab = (ImageView) findViewById(R.id.home);
        home_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitLibraryActivity.this,  MainPageActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });







        registerForContextMenu(habitList);
        habitList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                click_item_index=position;
                return false;
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "View Habit details");
        menu.add(0, v.getId(), 0, "View Habit Events");
        menu.add(0, v.getId(), 0, "Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");

        if (item.getTitle().equals("View Habit details")) {
            Intent i = new Intent(HabitLibraryActivity.this, ViewHabitActivity .class);
            i.putExtra("habit index", position);
            startActivity(i);
        }
        else if (item.getTitle().equals("View Habit Events")) {
            Intent i = new Intent(HabitLibraryActivity.this, HabitEventLibraryActivity .class);

            selectedHabit = fillist.get(position);
            hName = selectedHabit.getTitle();

            i.putExtra("habit name",hName);

            i.putExtra("habit index", position);
            startActivity(i);
        }

        else if (item.getTitle() == "Delete") {


            selectedHabit = fillist.get(position);
            hName = selectedHabit.getTitle();

            //Log.d("selected",selectedHabit.getTitle());

            ElasticSearchHabitController.DeleteHabitTask deleteHabitTask
                    = new ElasticSearchHabitController.DeleteHabitTask();
            deleteHabitTask.execute(selectedHabit);
///// delete corresponding events? ///////////////////

            ArrayList<HabitEvent> events  = new ArrayList<HabitEvent>();

/////// find events
            String event_query = "{\n" +
                    "  \"query\": { \n" +
                    "\"bool\": {\n"+
                    "\"must\": [\n"+
                    "{"+ " \"term\" : { \"uName\" : \"" + userName +  "\" }},\n" +
                    "{"+ " \"match\" : {  \"eName\" : \"" + hName +  "\" }}\n" +
                    "]"+
                    "}"+
                    "}"+
                    "}";

            ElasticSearchEventController.GetEvents getHEvent
                    = new  ElasticSearchEventController.GetEvents();
            getHEvent.execute(event_query);

            try {

                events.addAll(getHEvent.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            ////////// delete events //////
            for (HabitEvent element: events){
                ElasticSearchEventController.DeleteEventTask deleteEventTask
                        = new ElasticSearchEventController.DeleteEventTask();
                deleteEventTask.execute(element);

            }



////////////////////

            Toast.makeText(this, "Successfully deleted the habit! ", Toast.LENGTH_LONG).show();
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

        Log.d("Back", "start");

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");

        String MYhabits = "{\n" +
                "  \"query\": { \n" +
                " \"term\" : { \"uName\" : \"" + userName + "\" }\n" +
                " 	}\n" +
                "}";

        Log.d("MYhabits",MYhabits);

        ElasticSearchHabitController.GetHabits getHabits
                = new  ElasticSearchHabitController.GetHabits();
        getHabits.execute(MYhabits);

        try {
            fillist.clear();
            fillist.addAll(getHabits.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        adapter = new ArrayAdapter<Habit>(this, R.layout.list_habits, fillist);
        habitList.setAdapter(adapter);


    }




}



/*
public class HabitLibraryActivity extends AppCompatActivity {

    private ListView habitList;

    protected HabitList habits =new HabitList();

    protected ArrayAdapter<Habit> adapter;

    private int click_item_index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_library);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        final String userName = pref.getString("currentUser", "");

        habitList = (ListView)findViewById(R.id.habits);
        Button addButton = (Button) findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                //Log.d("user_habL",userName);
                Intent intent = new Intent(HabitLibraryActivity.this, AddHabitActivity.class);
                startActivity(intent);

            }
        });



        ImageView home_tab = (ImageView) findViewById(R.id.home);
        home_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitLibraryActivity.this,  MainPageActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });







        registerForContextMenu(habitList);
        habitList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                click_item_index=position;
                return false;
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "View Habit details");
        menu.add(0, v.getId(), 0, "View Habit Events");
        menu.add(0, v.getId(), 0, "Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        if (item.getTitle().equals("View Habit details")) {
            Intent i = new Intent(HabitLibraryActivity.this, ViewHabitActivity .class);
            i.putExtra("habit index", position);
            startActivity(i);
        }
        else if (item.getTitle().equals("View Habit Events")) {
            Intent i = new Intent(HabitLibraryActivity.this, HabitEventLibraryActivity .class);
            i.putExtra("habit index", position);
            startActivity(i);
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

            habits = user.getHabitList();
            habits.delete(habits.getHabit(click_item_index));

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

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");

        //Log.d("user_habL_start",userName);

        User user = new User();
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(userName);
        try {
            user = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }

        habits = user.getHabitList();
        adapter = new ArrayAdapter<Habit>(this, R.layout.list_habits, habits.getHabits());
        habitList.setAdapter(adapter);


    }









}

*/