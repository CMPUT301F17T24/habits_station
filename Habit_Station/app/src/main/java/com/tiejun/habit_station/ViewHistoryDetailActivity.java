/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;


/**
 * Activity to show see a specific history
 *
 * @author xuanyi
 * @version 1.5
 * @see HabitEvent
 * @see HabitEventList
 * @see Habit
 * @since 1.0
 *
 */
public class ViewHistoryDetailActivity extends AppCompatActivity {

    private TextView info;
    private ImageView image;
    private String imageBase64;

    /**
     *
     * find corresponding layouts
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history_detail);

        info = (TextView) findViewById(R.id.details);
        image = (ImageView)findViewById(R.id.image);
    }

    /**
     *
     * show the information
     *
     */
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        Intent intent = getIntent();
        String event_id = intent.getStringExtra("id");

        HabitEvent event = new HabitEvent();
        ElasticSearchEventController.GetEventTask getEventTask
                = new ElasticSearchEventController.GetEventTask();
        getEventTask.execute(event_id);
        try {
            event = getEventTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //  find the corresponding habit
        String habit_id = event.getuName() +event.geteName().toUpperCase();
        Habit habit = new Habit();

        ElasticSearchHabitController.GetHabitTask getHabit
                = new ElasticSearchHabitController.GetHabitTask();
        getHabit.execute(habit_id);
        try {
            habit = getHabit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // convert repeat week of day
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

        info.setText(habit.toString() +"\nReason: "+habit.getReason()+"\nPlan: "+sdays+"\n"+
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
