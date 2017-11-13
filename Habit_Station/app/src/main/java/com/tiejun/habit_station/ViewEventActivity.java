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
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

public class ViewEventActivity extends AppCompatActivity {

    protected HabitEventList habitEventList;
    private TextView info;
    private ArrayList<HabitEvent> fillist  = new ArrayList<HabitEvent>();

    User user = new User();
    HabitEvent event = new HabitEvent();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);


        ImageView delete_tab = (ImageView) findViewById(R.id.delete);

        delete_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ElasticSearchEventController.DeleteEventTask deleteEventTask
                            = new ElasticSearchEventController.DeleteEventTask();
                deleteEventTask.execute(event);

                Toast.makeText(getApplicationContext(), "Successfully deleted the event! ", Toast.LENGTH_SHORT).show();


            }
        });



    }



    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        Log.d("TTT","start");

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");
        Intent intent = getIntent();

        int eventIndex = intent.getIntExtra("select",0);
        final String habit_name = intent.getStringExtra("habit name");

//////  used to find the events /////////
        String event_query = intent.getStringExtra("query");
        /*String event_query = "{\n" +
                "  \"query\": { \n" +
                "\"bool\": {\n"+
                "\"must\": [\n"+
                "{"+ " \"term\" : { \"uName\" : \"" + userName +  "\" }},\n" +
                "{"+ " \"match\" : {  \"eName\" : \"" + habit_name +  "\" }}\n" +
                "]"+
                "}"+
                "}"+
                "}";
                */

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
/////// find that event
        event = fillist.get(eventIndex);
////////////////////

        info = (TextView) findViewById(R.id.details);

       /* info.setText(event.geteName() + " \nstarts " + event.getsTime().get(Calendar.YEAR)+"/"
                + String.valueOf(event.getsTime().get(Calendar.MONTH)+1) + "/" + event.getsTime().get(Calendar.DAY_OF_MONTH)
                +"\nReason: "+event.geteReason()+"\nPlan: "+event.getPlan()
                +"\nEvent finished at: "+ event.geteTime().get(Calendar.YEAR)+"/"
                + String.valueOf(event.geteTime().get(Calendar.MONTH)+1)
                + "/" + event.geteTime().get(Calendar.DAY_OF_MONTH)
                +"\nComment: "+event.geteComment());

*/

        String habit_id = userName +habit_name.toUpperCase();
        Habit habit = new Habit();
        ElasticSearchHabitController.GetHabitTask getHabit
                = new  ElasticSearchHabitController.GetHabitTask();
        getHabit.execute(habit_id);
        try {
            habit = getHabit.get();  //other way later
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
/////  find the corresponding habit

        HashSet<Integer> days = habit.getRepeatWeekOfDay();
        ArrayList<String> sdays = new ArrayList<String>();
        if (days.contains(1)){
            sdays.add("M");
        }
        if (days.contains(2)){
            sdays.add("T");
        }
        if (days.contains(3)){
            sdays.add("W");
        }
        if (days.contains(4)){
            sdays.add("R");
        }
        if (days.contains(5)){
            sdays.add("F");
        }
        if (days.contains(6)){
            sdays.add("SAT");
        }
        if (days.contains(0)){
            sdays.add("SUN");
        }

        info.setText(habit.toString() +"\nReason: "+habit.getReason()+"\nPlan: "+sdays+
                "\nEvent finished at: "+ event.geteTime().get(Calendar.YEAR)+"/"
                + String.valueOf(event.geteTime().get(Calendar.MONTH)+1)
                + "/" + event.geteTime().get(Calendar.DAY_OF_MONTH)
                +"\nComment: "+event.geteComment());



    }







}


/*
public class ViewEventActivity extends AppCompatActivity {

    protected HabitEventList habitEventList;
    private TextView info;

    User user = new User();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        final String userName = pref.getString("currentUser", "");
        Intent intent = getIntent();
        final int habitIndex = intent.getIntExtra("habit index", 0);
        final int eventIndex = intent.getIntExtra("select",0);

        final String habit_name = intent.getStringExtra("habit name");


        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(userName);
        try {
            user = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }


        ImageView add_tab = (ImageView) findViewById(R.id.add);
        add_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* habitEventList = user.getHabitList().getHabit(habitIndex).getHabitEventList();
                HabitEvent event = habitEventList.getEvent(eventIndex);

               HabitEventList history = user.getHistory();
                boolean exist = false;
                for (HabitEvent element: history.sortEvents()){
                    if (element.geteTime().equals(event.geteTime()) && (element.geteName().equals(event.geteName()))){
                        exist = true;
                    }
                }


                if (exist){
                    Toast.makeText(getApplicationContext(), "Already added to history.", Toast.LENGTH_SHORT).show();
                }
                else{
                    history.add(event);
                    HabitEventList history_sorted =  new HabitEventList(history.sortEvents());
                    user.setHistory(history_sorted);
                    ElasticSearchUserController.AddUserTask addUserTask
                            = new ElasticSearchUserController.AddUserTask();
                    addUserTask.execute(user);
                    Toast.makeText(getApplicationContext(), "Successfully added to history", Toast.LENGTH_SHORT).show();
                }

////////
                HabitEvent event = habitEventList.getEvent(eventIndex);
                event.setuName(userName);

                String id = userName+event.geteName()
                        +event.geteTime().get(Calendar.YEAR)
                        +String.valueOf(event.geteTime().get(Calendar.MONTH)+1)
                        +event.geteTime().get(Calendar.DAY_OF_MONTH);

                if (existedEvent(id)){

//                    ElasticSearchEventController.DeleteEventTask deleteEventTask
//                            = new ElasticSearchEventController.DeleteEventTask();
//                    deleteEventTask.execute(event);
//

                    Toast.makeText(getApplicationContext(), "Already added to history.", Toast.LENGTH_SHORT).show();
                }
                else {

                    ElasticSearchEventController.AddEventTask addEventTask
                            = new ElasticSearchEventController.AddEventTask();
                    addEventTask.execute(event);
                    Toast.makeText(getApplicationContext(), "Successfully added to history", Toast.LENGTH_SHORT).show();
                }





            }
        });








    }



    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        Log.d("TTT","start");

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");
        Intent intent = getIntent();
        int habitIndex = intent.getIntExtra("habit index", 0);
        int eventIndex = intent.getIntExtra("select",0);


        //User user = new User();
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(userName);
        try {
            user = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }



        habitEventList = user.getHabitList().getHabit(habitIndex).getHabitEventList();
        HabitEvent event = habitEventList.getEvent(eventIndex);


        info = (TextView) findViewById(R.id.details);
        Habit habit = user.getHabitList().getHabit(habitIndex);
        //String habit_name  = habit.getTitle();
        HashSet<Integer> days = habit.getRepeatWeekOfDay();
        ArrayList<String> sdays = new ArrayList<String>();
        if (days.contains(1)){
            sdays.add("M");
        }
        if (days.contains(2)){
            sdays.add("T");
        }
        if (days.contains(3)){
            sdays.add("W");
        }
        if (days.contains(4)){
            sdays.add("R");
        }
        if (days.contains(5)){
            sdays.add("F");
        }
        if (days.contains(6)){
            sdays.add("SAT");
        }
        if (days.contains(0)){
            sdays.add("SUN");
        }

        info.setText(habit.toString() +"\nReason: "+habit.getReason()+"\nPlan: "+sdays+
                "\nEvent finished at: "+ event.geteTime().get(Calendar.YEAR)+"/"
                + String.valueOf(event.geteTime().get(Calendar.MONTH)+1)
                + "/" + event.geteTime().get(Calendar.DAY_OF_MONTH)
                +"\nComment: "+event.geteComment());


    }




    private boolean existedEvent (String id) {
        ElasticSearchEventController.IsExist isExist = new ElasticSearchEventController.IsExist();
        isExist.execute(id);

        try {
            if (isExist.get()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }


    }






}
*/