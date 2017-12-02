/*
 * Copyright (c) 2017 Team24, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of license in this project. Otherwise please contact xuanyi@ualberta.ca.
 */

package com.tiejun.habit_station;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

/**
 * Activity to view habit events
 *
 * @author xuanyi
 * @version 1.5
 * @see HabitEvent
 * @see HabitEventList
 * @since 1.0
 *
 */

public class ViewEventActivity extends AppCompatActivity {

    //protected HabitEventList habitEventList;
    private TextView info;
    private ArrayList<HabitEvent> fillist  = new ArrayList<HabitEvent>();
    private ImageView image;
    private String imageBase64;

    User user = new User();
    HabitEvent event = new HabitEvent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        info = (TextView) findViewById(R.id.details);
        image = (ImageView)findViewById(R.id.image);

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


        String event_query = intent.getStringExtra("query");
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

        // find that event
        event = fillist.get(eventIndex);

        String habit_id = userName +habit_name.toUpperCase();
        Habit habit = new Habit();
        ElasticSearchHabitController.GetHabitTask getHabit
                = new  ElasticSearchHabitController.GetHabitTask();
        getHabit.execute(habit_id);

        try {
            habit = getHabit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //  find the corresponding habit
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
        String eventLocation;
        if (event.geteLocation() != null){
            eventLocation = event.geteLocation().toString();
        }
        else{
            eventLocation = "";
        }

        info.setText(habit.toString() +"\nReason: "+habit.getReason()+"\nPlan: "+sdays+
                "\nEvent finished at: "+ event.geteTime().get(Calendar.YEAR)+"/"
                + String.valueOf(event.geteTime().get(Calendar.MONTH)+1)
                + "/" + event.geteTime().get(Calendar.DAY_OF_MONTH)
                +"\nComment: "+event.geteComment()
                +"\nLocation: "+ eventLocation );

        imageBase64 = event.getePhoto();
        if ( imageBase64 !=null) {
            image.setImageBitmap(base64ToImage());
        }
        else{
            image.setImageBitmap(null);

        }
    }

    /**
     * Convert base64 to bitmap object
     * @return
     */
    public Bitmap base64ToImage() {
        byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
                decodedString.length);
        return decodedByte;
    }
}
